package micro.apps.shared.dsl

import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.coders.NullableCoder
import org.apache.beam.sdk.io.FileIO
import org.apache.beam.sdk.io.TextIO
import org.apache.beam.sdk.options.PipelineOptions
import org.apache.beam.sdk.options.PipelineOptionsFactory
import org.apache.beam.sdk.transforms.*
import org.apache.beam.sdk.values.*

fun <A, B> ((A) -> B).toSerializableFunction(): SerializableFunction<A, B> = SerializableFunction { this(it) }

/**
 * Adapted and extended from https://github.com/Dan-Dongcheol-Lee/apachebeam-kotlin-blog-examples
 */
object KPipeline {
    inline fun <reified R : PipelineOptions> from(args: Array<String>): Pair<Pipeline, R> {
        val options = PipelineOptionsFactory.fromArgs(*args)
                .withValidation()
                .`as`(R::class.java)
        return Pipeline.create(options) to options
    }
}


/**
 * Read data from a text file.
 */
fun Pipeline.fromText(name: String = "Read from Text", path: String): PCollection<String> {
    return this.apply(name, TextIO.read().from(path))
}

/**
 * Write data to a text file.
 */
fun PCollection<String>.toText(name: String = "Write to Text", filename: String): PDone {
    return this.apply(name, TextIO.write().to(filename))
}

/**
 * Read from File Collection.
 */
fun Pipeline.fromFiles(
        name: String = "Read from File Collection",
        input: String): PCollection<KV<String, String>> {

    return this.apply(name, FileIO.match().filepattern(input))
            .apply("$name readMatches", FileIO.readMatches())
            .apply("$name read files", MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings())).via(SerializableFunction { file: FileIO.ReadableFile ->
                KV.of(file.metadata.resourceId().toString(), file.readFullyAsUTF8String())
            }))
}


/**
 * Run a function on each member of a PCollect
 */
inline fun <I, reified O> PCollection<I>.map(
        name: String = "map to ${O::class.simpleName}",
        noinline transform: (I) -> O): PCollection<O> {
    val pc = this.apply(name,
            MapElements.into(TypeDescriptor.of(O::class.java))
                    .via(transform.toSerializableFunction()))
    return pc.setCoder(NullableCoder.of(pc.coder))
}


/**
 * Convert a nested PCollect into a single PCollect
 */
inline fun <I, reified O> PCollection<I>.flatMap(
        name: String = "flatMap to ${O::class.simpleName}",
        noinline transform: (I) -> Iterable<O>): PCollection<O> {
    val pc = this.apply(name, FlatMapElements.into(TypeDescriptor.of(O::class.java))
            .via(transform.toSerializableFunction()))
    return pc.setCoder(NullableCoder.of(pc.coder))
}

/**
 * Filter items in a PCollect.
 */
fun <I> PCollection<I>.filter(
        name: String = "Filter items",
        transform: (I) -> Boolean): PCollection<I> {
    val pc = this.apply(name, Filter.by(transform.toSerializableFunction()))
    return pc.setCoder(NullableCoder.of(pc.coder))
}


fun <I> PCollection<I>.countPerElement(
        name: String = "count per element"): PCollection<KV<I, Long>> {
    return this.apply(name, Count.perElement<I>())
            .setTypeDescriptor(object : TypeDescriptor<KV<I, Long>>() {})
}

/**
 * Generic DoFn context implementation
 */
open class DoFnContext<I, O>(val context: DoFn<I, O>.ProcessContext) {

    val options: PipelineOptions
        get() = context.pipelineOptions

    val element: I
        get() = context.element()

    fun output(item: O) {
        context.output(item)
    }

    fun <T> outputTagged(tag: TupleTag<T>, item: T) {
        context.output(tag, item)
    }
}

/**
 * Generic parDo implementation.
 */
inline fun <I, reified O> PCollection<I>.parDo(
        name: String = "ParDo to ${O::class.simpleName}",
        crossinline transform: DoFnContext<I, O>.() -> Unit): PCollection<O> {
    val pc = this.apply(name,
            ParDo.of(object : DoFn<I, O>() {
                @DoFn.ProcessElement
                fun processElement(context: ProcessContext) {
                    DoFnContext(context).apply(transform)
                }
            }
            ))
    return pc.setCoder(NullableCoder.of(pc.coder))
}