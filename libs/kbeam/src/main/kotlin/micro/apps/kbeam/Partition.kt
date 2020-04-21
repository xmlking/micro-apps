package micro.apps.kbeam

import org.apache.beam.sdk.transforms.Partition
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PCollectionList

inline fun <reified I> PCollection<I>.partition(nPartitions: Int, crossinline function: (item: I) -> Int): PCollectionList<I> {
    return this.apply(Partition.of(nPartitions) { item, _ ->
        function(item)
    })
}
