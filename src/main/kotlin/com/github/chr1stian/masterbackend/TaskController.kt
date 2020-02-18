package com.github.chr1stian.masterbackend

import javafx.fxml.FXML
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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


}