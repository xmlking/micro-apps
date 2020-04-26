package micro.apps.kbeam

import org.apache.beam.sdk.transforms.View
import org.apache.beam.sdk.values.KV
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PCollectionView

/**
 * Converts the input PCollection into a list based PCollectionView
 *
 * **Warning** The output will be memory bound, use [toIterable] for large sets
 */
fun <I> PCollection<I>.toList(): PCollectionView<List<I>> {
    return this.apply(View.asList())
}

/**
 * Converts the input PCollection into an Iterable based PCollectionView
 *
 * Runtime environment *should* optimize for disk based stores and avoid storing the broadcast collection on the Heap
 */
fun <I> PCollection<I>.toIterable(): PCollectionView<Iterable<I>> {
    return this.apply(View.asIterable())
}

fun <K, V> PCollection<KV<K, V>>.toMap(): PCollectionView<Map<K, V>> {
    return this.apply(View.asMap())
}

fun <I> PCollection<I>.asSingleton(): PCollectionView<I> {
    return this.apply(View.asSingleton())
}
