package com.github.chr1stian.masterbackend

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource

fun buildTask(task: Task): DOMSource {
        var dbf = DocumentBuilderFactory.newInstance()
        var db = dbf.newDocumentBuilder()
        var doc = db.parse("/Users/christiannyvoll/Documents/Master/xml/inline-gap-match/inline_gap_match.xml")
        var assessmentItem = doc.firstChild


        val gapMatchInteraction = doc.getElementsByTagName("gapMatchInteraction")
        var gapText = gapMatchInteraction.item(0).childNodes.item(3)

        var span = gapText.childNodes.item(1)

        span.textContent = task.splitCode[1][1]
        var source = DOMSource(doc)
        return source
    }