package micro.apps.kbeam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SamplePipelineTest implements Serializable {
    private static ThreadLocal<ObjectMapper> json = ThreadLocal.withInitial(ObjectMapper::new);

    @Rule
    public final transient TestPipeline testPipeline = TestPipeline.create();

    @Test
    public void runTest() {
        PipelineOptionsFactory.register(MyOptions.class);
        MyOptions options = PipelineOptionsFactory.fromArgs("--test=toto").withValidation().as(MyOptions.class);

        assertEquals(options.getTest(), "toto");

        Pipeline pipeline = Pipeline.create(options);

        // Load mapping of country code to country
        PCollection<String> countryCodes = pipeline.apply("Read Country File", TextIO.read().from("src/test/resources/data/country_codes.jsonl"));
        PCollection<KV<String, String>> countryCodesKV = countryCodes.apply("Parse", MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings())).via(
            (String line) -> {
                try {
                    JsonNode jsonLine = json.get().readTree(line);
                    return KV.of(jsonLine.get("code").getTextValue(), jsonLine.get("name").getTextValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        ));

        PCollectionView<Map<String, String>> countryCodesView = countryCodesKV.apply(View.asMap());


        // Load list of lines
        PCollection<String> lines = pipeline.apply("Read lines", TextIO.read().from("src/test/resources/data/test.csv"));
        PCollection<Entry> entries = lines.apply("Map to entries", ParDo.of(new DoFn<String, Entry>() {
            @ProcessElement
            public void process(ProcessContext context) {
                String[] words = context.element().split(",");
                if (words.length == 3) {
                    context.output(new Entry(words[0], words[1], Double.parseDouble(words[2])));
                }
            }
        }));

        // Fill in country names
        PCollection<Entry> entries2 = entries.apply("Enrich with country name", ParDo.of(new DoFn<Entry, Entry>() {
            @ProcessElement
            public void process(ProcessContext context) {
                String code = context.element().getCountryCode();
                String name = context.sideInput(countryCodesView).getOrDefault(code, "unknown");
                context.output(new Entry(context.element().getName(), context.element().getCountryCode(), context.element().getDoubleValue(), name));
            }
        }).withSideInputs(countryCodesView));

        // split in two collections
        TupleTag<Entry> positiveEntries = new TupleTag<Entry>() {
        };
        TupleTag<Entry> negativeEntries = new TupleTag<Entry>() {
        };

        PCollectionTuple splitResult = entries2.apply("Split positives and negatives", ParDo.of(new DoFn<Entry, Entry>() {
            @ProcessElement
            public void process(ProcessContext context) {
                if (context.element().getDoubleValue() >= 0) {
                    context.output(positiveEntries, context.element());
                } else {
                    context.output(negativeEntries, context.element());
                }
            }
        }).withOutputTags(positiveEntries, TupleTagList.of(negativeEntries)));

        // this is an example so let's just print the results

        splitResult.get(positiveEntries).apply("Positive", ParDo.of(new DoFn<Entry, Void>() {
            @ProcessElement
            public void process(ProcessContext context) {
                System.out.println("Positive: " + context.element().toString());
            }
        }));

        splitResult.get(negativeEntries).apply("Negative", ParDo.of(new DoFn<Entry, Void>() {
            @ProcessElement
            public void process(ProcessContext context) {
                System.out.println("Negative: " + context.element().toString());
            }
        }));

        pipeline.run(options).waitUntilFinish();

    }

}

