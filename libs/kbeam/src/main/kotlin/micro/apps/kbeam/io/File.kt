package micro.apps.kbeam.io

import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.io.Compression
import org.apache.beam.sdk.io.FileIO
import org.apache.beam.sdk.io.TextIO
import org.apache.beam.sdk.options.ValueProvider
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.SerializableFunction
import org.apache.beam.sdk.transforms.Watch
import org.apache.beam.sdk.values.KV
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.TypeDescriptors
import org.joda.time.Duration

fun <A, B> ((A) -> B).toSerializableFunction(): SerializableFunction<A, B> = SerializableFunction { this(it) }

open class WriteConfig(
    var path: ValueProvider<String>? = null,
    var compression: Compression = Compression.UNCOMPRESSED,
    var suffix: String = ".txt",
    var numShards: Int = 0
) {
    operator fun <DT, UT> invoke(io: FileIO.Write<DT, UT>): FileIO.Write<DT, UT>? {
        return io
            .to(path)
            .withCompression(compression)
            .withNumShards(numShards)
            .withSuffix(suffix)
    }
}

open class TextWriteConfig(
    var delimiter: String = "\r\n",
    var header: String = "",
    var footer: String = ""
) : WriteConfig() {
    operator fun invoke(io: TextIO.Write): TextIO.Write {
        return io
            .to(path)
            .withCompression(compression)
            .withNumShards(numShards)
            .withSuffix(suffix)
            .withDelimiter(delimiter.toCharArray())
            .withHeader(header)
            .withFooter(footer)
    }
}

class TextReadConfig(
    var filePattern: ValueProvider<String>? = null,
    var compression: Compression = Compression.UNCOMPRESSED,
//    var delimiter: ByteArray = "\r\n".toByteArray(Charsets.US_ASCII),
    var delimiter: ByteArray = "\n".toByteArray(Charsets.US_ASCII),
    var watchForNewFiles: Boolean = false,
    var checkPeriod: Duration = Duration.standardMinutes(1)
) {
    operator fun invoke(io: TextIO.Read): TextIO.Read {
        if (watchForNewFiles) {
            // Streaming case
            @Suppress("INACCESSIBLE_TYPE")
            return io
                .from(filePattern)
                .withCompression(compression)
                .withDelimiter(delimiter)
                .watchForNewFiles(checkPeriod, Watch.Growth.never())
        } else {
            return io
                .from(filePattern)
                .withCompression(compression)
                .withDelimiter(delimiter)
        }
    }
}

/**
 * Reads a text files as a PCollection of lines
 */
fun Pipeline.readTextFile(name: String? = "Read from Text", configurator: TextReadConfig.() -> Unit): PCollection<String> {
    val readConfig: TextReadConfig = TextReadConfig()
    configurator(readConfig)
    if (readConfig.watchForNewFiles) {
        // Streaming case
        return this.apply(name ?: "Read from ${readConfig.filePattern}",
            readConfig(TextIO.read()))
    } else {
        // Batch case
        return this.apply(name ?: "Read from ${readConfig.filePattern}",
            readConfig(TextIO.read()))
    }
}

fun PCollection<String>.writeText(name: String? = "Write to Text", configFunction: (TextWriteConfig.() -> Unit)) {
    val config = TextWriteConfig()
    configFunction(config)
    if (config.path == null) {
        throw IllegalArgumentException("Must define a file path")
    }
    val t = config(TextIO.write())
    this.apply(name ?: "Writing to ${config.path}", t)
}

/**
 * Read files from a PCollection of file names
 */
fun PCollection<String>.readAllTextFiles(name: String? = null): PCollection<String> {
    return this.apply(name ?: "Read from File Collection", TextIO.readAll()
        .withCompression(Compression.AUTO))
}

/**
 * Read from File Collection.
 */
fun Pipeline.fromFiles(
    name: String = "Read from File Collection",
    input: String
): PCollection<KV<String, String>> {

    return this.apply(name, FileIO.match().filepattern(input))
        .apply("$name readMatches", FileIO.readMatches())
        .apply(
            "$name read files",
            MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
                .via(SerializableFunction { file: FileIO.ReadableFile ->
                    KV.of(file.metadata.resourceId().toString(), file.readFullyAsUTF8String())
                })
        )
}
