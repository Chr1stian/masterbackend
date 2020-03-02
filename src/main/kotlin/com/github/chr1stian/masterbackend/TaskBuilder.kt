package com.github.chr1stian.masterbackend

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource

fun buildTask(task: Task): DOMSource {
    val dbf = DocumentBuilderFactory.newInstance()
    val db = dbf.newDocumentBuilder()
    val doc = db.parse("/Users/christiannyvoll/Documents/Master/xml/inline-gap-match/inline_gap_match.xml")
    var assessmentItem = doc.firstChild

    // GapText | The drag/droppable elements
    val gapMatchInteraction = doc.getElementsByTagName("gapMatchInteraction")
    val gapText = gapMatchInteraction.item(0).childNodes.item(3)
    val span = gapText.childNodes.item(1)
    span.textContent = task.splitCode[1][1]

    // Blockquote | The surrounding text/code with GAPS (drop-fields)
    val blockquote = doc.getElementsByTagName("blockquote").item(0)
    for (item in task.splitCode) {
        if (item.size == 1) {
            val element = doc.createElement("p")
            element.appendChild(doc.createTextNode(item[0]))
            blockquote.appendChild(element)
        } else {
            val element = doc.createElement("p")
            element.appendChild(doc.createTextNode(item[0]))
            val gap = doc.createElement("gap")
            gap.setAttribute("identifier", "GAP1")
            element.appendChild(gap)
            element.appendChild(doc.createTextNode(item[2]))
            blockquote.appendChild(element)
        }

    }
    blockquote.childNodes.item(1).textContent = task.splitCode[1][0] + task.splitCode[1][2]


    var source = DOMSource(doc)
    return source
}