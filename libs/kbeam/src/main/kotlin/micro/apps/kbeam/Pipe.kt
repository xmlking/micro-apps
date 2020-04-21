package micro.apps.kbeam

/* ktlint-disable no-wildcard-imports */
import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.options.PipelineOptions
import org.apache.beam.sdk.options.PipelineOptionsFactory
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PCollection

/**
 * Adapted and extended from https://github.com/Dan-Dongcheol-Lee/apachebeam-kotlin-blog-examples
 */

/**
 * Utility methods for Pipelines
 */
object PipeBuilder {
    /**
     * Create a configured pipeline from arguments
     * Custom options class should be passed as a type parameter
     * @return (the pipeline, typed options)
     */
    inline fun <reified R : PipelineOptions> from(args: Array<String>): Pair<Pipeline, R> {
        val options = PipelineOptionsFactory.fromArgs(*args)
            .withValidation()
            .`as`(R::class.java)
        return Pipeline.create(options) to options
    }
}

/**
 * Create a composite PTransform from a chain of PTransforms
 */
fun <I, O> combine(combiner: (col: PCollection<I>) -> PCollection<O>): PTransform<PCollection<I>, PCollection<O>> {
    return object : PTransform<PCollection<I>, PCollection<O>>() {
        override fun expand(input: PCollection<I>): PCollection<O> {
            return combiner(input)
        }
    }
}
