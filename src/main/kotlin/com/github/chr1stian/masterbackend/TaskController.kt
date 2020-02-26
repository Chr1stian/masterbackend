package com.github.chr1stian.masterbackend


import javafx.fxml.FXML
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
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
    fun createTasks(@Valid @RequestBody input: Any){
        println("Request to create task: $input")
    }



}