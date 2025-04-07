import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.Inflater
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.

    if (args.isEmpty()) {
        println("Usage: your_program.sh <command> [<args>]")
        exitProcess(1)
    }

    if (args[0] == "init") {
        val gitDir = File(".git")
        gitDir.mkdir()
        File(gitDir, "objects").mkdir()
        File(gitDir, "refs").mkdir()
        File(gitDir, "HEAD").writeText("ref: refs/heads/master\n")
        println("Initialized git directory")
    } else if (args[0] == "cat-file") {
        if (args[1] == "-p") {
            val hash = args[2]
            val gitDir = File(".git")
            val data = decompress("${File(gitDir, "objects").absolutePath}/${hash.substring(0, 2)}/${hash.substring(2, hash.length)}")
            val splitData = data.split('\u0000')
            print(splitData[1])
        }
   } else {
        println("Unknown command: ${args[0]}")
        exitProcess(1)
    }
}

fun decompress(filepath: String): String {
    val content = File(filepath).readBytes()
    val inflater = Inflater()
    val outputStream = ByteArrayOutputStream()
    inflater.setInput(content)
    val buffer = ByteArray(1024)
    while (!inflater.finished()) {
        val count = inflater.inflate(buffer)
        outputStream.write(buffer, 0, count)
    }
    outputStream.close()
    return outputStream.toString(Charsets.UTF_8)
}