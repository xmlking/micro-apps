package micro.apps.kbeam

import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.transforms.join.CoGroupByKey
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple
import org.apache.beam.sdk.values.KV
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.TupleTag

@Suppress("unused")
inline fun <reified KeyType, reified LeftType, reified RightType, reified OutputType> Pipeline.coGroupByKey(
    left: PCollection<KV<KeyType, LeftType>>,
    right: PCollection<KV<KeyType, RightType>>,
    crossinline function: (key: KeyType, left: Iterable<LeftType>, right: Iterable<RightType>) -> List<OutputType>
):
    PCollection<OutputType> {
    val leftTag = object : TupleTag<LeftType>() {}
    val rightTag = object : TupleTag<RightType>() {}
    val keyedPCollectionTuple = KeyedPCollectionTuple.of(leftTag, left).and(rightTag, right)
    return keyedPCollectionTuple.apply(CoGroupByKey.create()).parDo {
        val k = element.key
        if (k != null) {
            function(k, element.value.getAll(leftTag), element.value.getAll(rightTag)).forEach { output(it) }
        }
    }
}
