package micro.apps.kbeam

import org.apache.beam.sdk.coders.NullableCoder
import org.apache.beam.sdk.options.PipelineOptions
import org.apache.beam.sdk.transforms.Count
import org.apache.beam.sdk.transforms.DoFn
import org.apache.beam.sdk.transforms.Filter
import org.apache.beam.sdk.transforms.FlatMapElements
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.ParDo
import org.apache.beam.sdk.transforms.SerializableFunction
import org.apache.beam.sdk.transforms.windowing.PaneInfo
import org.apache.beam.sdk.values.KV
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PCollectionView
import org.apache.beam.sdk.values.TupleTag
import org.apache.beam.sdk.values.TupleTagList
import org.apache.beam.sdk.values.TypeDescriptor
import org.joda.time.Instant

fun <A, B> ((A) -> B).toSerializableFunction(): SerializableFunction<A, B> = SerializableFunction { this(it) }

/**
 * Utility class to access sideInputs as a "map like" object from a process context
 * @param context The context to wrap
 */
class SideInputs<I>(private val context: DoFnContext<I, *>) {
    /**
     * "Map like" Access to side input views
     */
    operator fun <T> get(view: PCollectionView<T>): T {
        return context.context.sideInput(view)
    }
}

/**
 * DoFn.ProcessContext interface
 *
 * Clean interface replacing the original abstract class for easier delegation and extensions
 */
interface DoFnContext<I, O> {
    val context: DoFn<I, O>.ProcessContext

    val options: PipelineOptions
    val element: I
    val sideInputs: SideInputs<I>
    val timestamp: Instant
    val pane: PaneInfo
//    fun updateWatermark(watermark: Instant)

    fun output(item: O)
    fun outputTimeStamped(item: O, timestamp: Instant)
    fun <T> outputTagged(tag: TupleTag<T>, item: T)
    fun <T> outputTaggedTimestamped(tag: TupleTag<T>, item: T, timestamp: Instant)
}

open class DoFnContextWrapper<I, O>(override val context: DoFn<I, O>.ProcessContext) : DoFnContext<I, O> {

    override val options: PipelineOptions
        get() = context.pipelineOptions

    override val element: I
        get() = context.element()

    @Suppress("LeakingThis")
    final override val sideInputs: SideInputs<I> = SideInputs(this)

    override val timestamp: Instant
        get() = context.timestamp()
    override val pane: PaneInfo
        get() = context.pane()

//    override fun updateWatermark(watermark: Instant) {
//        context.updateWatermark(watermark)
//    }

    override fun output(item: O) {
        context.output(item)
    }

    override fun outputTimeStamped(item: O, timestamp: Instant) {
        context.outputWithTimestamp(item, timestamp)
    }

    override fun <T> outputTagged(tag: TupleTag<T>, item: T) {
        context.output(tag, item)
    }

    override fun <T> outputTaggedTimestamped(tag: TupleTag<T>, item: T, timestamp: Instant) {
        context.outputWithTimestamp(tag, item, timestamp)
    }
}

/**
 * Generic parDo extension method
 *
 * The executed lambda has access to an implicit process context as *this*
 * @param name The name of the processing step
 * @param sideInputs The *optional* sideInputs
 */
inline fun <I, reified O> PCollection<I>.parDo(
    name: String = "ParDo to ${O::class.simpleName}",
    sideInputs: List<PCollectionView<*>> = emptyList(),
    crossinline function: DoFnContext<I, O>.() -> Unit
):
    PCollection<O> {
    return this.apply(
        name,
        ParDo.of(object : DoFn<I, O>() {
            @ProcessElement
            fun processElement(processContext: ProcessContext) {
                DoFnContextWrapper(processContext).apply(function)
            }
        }).withSideInputs(sideInputs)
    )
}

/**
 * Filter the input PCollection by a condition
 *
 * The executed lambda has access to an implicit process context as *this* if needed
 */
inline fun <reified I> PCollection<I>.filter(
    name: String = "filter",
    crossinline function: (I) -> Boolean
): PCollection<I> {
    return this.parDo(name) {
        if (function(element)) {
            output(element)
        }
    }
}

/**
 * Map input PCollection
 * nulls are suppressed by default from the output
 * The executed lambda has access to an implicit process context as *this* if needed
 */
inline fun <I, reified O> PCollection<I>.map(
    name: String = "map to ${O::class.simpleName}",
    crossinline function: (I) -> O
): PCollection<O> {
    return this.parDo(name) {
        val o = function(element)
        if (o != null) {
            output(function(element))
        }
    }
}

/**
 * FlatMap input PCollection
 * The executed lambda has access to an implicit process context as *this* if needed
 */
inline fun <I, reified O> PCollection<I>.flatMap(
    name: String = "flatMap to ${O::class.simpleName}",
    crossinline function: (I) -> Iterable<O>
): PCollection<O> {
    return this.parDo(name) {
        val l = function(element)
        l.forEach {
            output(it)
        }
    }
}

inline fun <reified I> PCollection<I>.split(
    name: String = "split",
    crossinline function: (I) -> Boolean
): DoFn2Outputs<I, I> {
    return this.parDo2(name) {
        if (function(element)) {
            output(element)
        } else {
            output2(element)
        }
    }
}

/**
 * Generic parDo extension method for 2 outputs
 *
 * The executed lambda has access to an implicit process context as *this* with typed outputs: *output* and *output2*
 * @param name The name of the processing step
 * @param sideInputs The *optional* sideInputs
 */
inline fun <I, reified O1, reified O2> PCollection<I>.parDo2(
    name: String = "ParDo",
    sideInputs: List<PCollectionView<*>> = emptyList(),
    crossinline function: DoFnContextWrapper2Outputs<I, O1, O2>.() -> Unit
): DoFn2Outputs<O1, O2> {
    val output1Tag = object : TupleTag<O1>() {}
    val output2Tag = object : TupleTag<O2>() {}
    val tagList = TupleTagList.of(listOf(output2Tag))
    val pCollectionTuple = this.apply(
        name,
        ParDo.of(object : DoFn<I, O1>() {
            @ProcessElement
            fun processElement(context: ProcessContext) {
                DoFnContextWrapper2Outputs(context, output2Tag).apply(function)
            }
        }).withSideInputs(sideInputs).withOutputTags(output1Tag, tagList)
    )
    return DoFn2Outputs(pCollectionTuple[output1Tag], pCollectionTuple[output2Tag])
}

data class DoFn2Outputs<O1, O2>(val output1: PCollection<O1>, val output2: PCollection<O2>)

class DoFnContextWrapper2Outputs<I, O1, O2>(
    processContext: DoFn<I, O1>.ProcessContext,
    private val tag2: TupleTag<O2>
) : DoFnContextWrapper<I, O1>(processContext) {

    fun output2(item: O2) {
        outputTagged(tag2, item)
    }

    fun output2Timestamped(item: O2, timestamp: Instant) {
        outputTaggedTimestamped(tag2, item, timestamp)
    }
}

/** OLD **/

/**
 * Run a function on each member of a PCollect
 */
inline fun <I, reified O> PCollection<I>.map0(
    name: String = "map to ${O::class.simpleName}",
    noinline transform: (I) -> O
): PCollection<O> {
    val pc = this.apply(
        name,
        MapElements.into(TypeDescriptor.of(O::class.java))
            .via(transform.toSerializableFunction())
    )
    return pc.setCoder(NullableCoder.of(pc.coder))
}

/**
 * Convert a nested PCollect into a single PCollect
 */
inline fun <I, reified O> PCollection<I>.flatMap0(
    name: String = "flatMap to ${O::class.simpleName}",
    noinline transform: (I) -> Iterable<O>
): PCollection<O> {
    val pc = this.apply(
        name,
        FlatMapElements.into(TypeDescriptor.of(O::class.java))
            .via(transform.toSerializableFunction())
    )
    return pc.setCoder(NullableCoder.of(pc.coder))
}

/**
 * Filter items in a PCollect.
 */
fun <I> PCollection<I>.filter0(
    name: String = "Filter items",
    transform: (I) -> Boolean
): PCollection<I> {
    val pc = this.apply(name, Filter.by(transform.toSerializableFunction()))
    return pc.setCoder(NullableCoder.of(pc.coder))
}

fun <I> PCollection<I>.countPerElement(
    name: String = "count per element"
): PCollection<KV<I, Long>> {
    return this.apply(name, Count.perElement<I>())
        .setTypeDescriptor(object : TypeDescriptor<KV<I, Long>>() {})
}
