package com.github.chr1stian.masterbackend


import javafx.fxml.FXML
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.servlet.http.HttpServletResponse
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource


@RestController
@RequestMapping("/api")
class TaskController {

    @FXML
    @RequestMapping("/task/{title}")
    fun getTask(@PathVariable title: String): DOMSource {
        var dbf = DocumentBuilderFactory.newInstance()
        var db = dbf.newDocumentBuilder()
        var doc = db.parse("/Users/christiannyvoll/Documents/Master/xml/ID_54074230-item.xml")

        println("title: $title")

        var assessmentItem = doc.firstChild
        var attr = assessmentItem.attributes
        var nodeAttr = attr.getNamedItem("title")
        nodeAttr.textContent = title

        var tf = TransformerFactory.newInstance()
        var t = tf.newTransformer()
        var source = DOMSource(doc)
        // Saves changes to old file
        // var result = StreamResult(File("/Users/christiannyvoll/Documents/Master/xml/ID_54074230-item.xml"))
        // t.transform(source, result)
        return source
    }

    @RequestMapping("/tasks/{input}")
    fun getTasks(@PathVariable input: Any) {
        println(input)
    }

    @PostMapping("/tasks")
    fun createTasks(@RequestBody input: Task): String {
        println("Request to create task: $input")

        return "Task created with following task label: ${input.label}"
    }

    @PostMapping("/test")
    fun test(@RequestBody input: Task): DOMSource {
        println("Request to create task: $input")

        val builtTask = buildTask(input)
        return builtTask
    }

    @PostMapping(value = ["/zip-download"], produces = ["application/zip"])
    @Throws(IOException::class)
    fun zipDownload(@RequestParam name: List<String>, @RequestBody task: Task, response: HttpServletResponse) {
        val zipOut = ZipOutputStream(response.outputStream)
        for (fileName in name) {
            val fileBasePath = "/Users/christiannyvoll/Documents/Master/master-backend/"
            val resource = FileSystemResource(fileBasePath + fileName)
            val zipEntry = ZipEntry(resource.filename)
            zipEntry.size = resource.contentLength()
            zipOut.putNextEntry(zipEntry)
            StreamUtils.copy(resource.inputStream, zipOut)
            zipOut.closeEntry()
        }
        zipOut.finish()
        zipOut.close()
        response.status = HttpServletResponse.SC_OK
        val zipFileName = "generatedZip"
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$zipFileName\"")
    }
}