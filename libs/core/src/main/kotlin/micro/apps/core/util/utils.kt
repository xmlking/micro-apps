package micro.apps.core.util

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Decompress a Gzip-compressed byte array
 * @param data Data to decompress
 */
fun decompressGzip(data: ByteArray): String {
    return GZIPInputStream(data.inputStream()).bufferedReader(UTF_8).use { it.readText() }
}

/**
 * Compress a string to Gzip
 * @param data Data to compress
 */
fun compressGzip(data: String): ByteArray {
    val os = ByteArrayOutputStream()
    GZIPOutputStream(os).bufferedWriter(UTF_8).use { it.write(data) }
    return os.toByteArray()
}
