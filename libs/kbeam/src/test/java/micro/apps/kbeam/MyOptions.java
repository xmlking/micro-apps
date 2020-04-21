package micro.apps.kbeam;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface MyOptions extends PipelineOptions {
    @Description("Custom Option")
    @Default.String("test")
    String getTest();

    void setTest(String test);
}
