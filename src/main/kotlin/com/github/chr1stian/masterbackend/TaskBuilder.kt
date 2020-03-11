package com.github.chr1stian.masterbackend

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource

fun buildTask(task: Task): DOMSource {
    val dbf = DocumentBuilderFactory.newInstance()
    val db = dbf.newDocumentBuilder()
    val doc = db.parse("/Users/christiannyvoll/Documents/Master/xml/inline-gap-match/inline_gap_match.xml")

    val responseDeclaration: Node = doc.getElementsByTagName("responseDeclaration").item(0)
    val correctResponse = doc.createElement("correctResponse")
    val mapping = doc.createElement("mapping")
    mapping.setAttribute("defaultValue", "0")
    val blockquote = doc.createElement("blockquote")

    var number: Int = 1
    for (code in task.splitCode) {
        if (code.size != 1) {
            createGapText(doc, code, number)


            // mapResponseDeclaration(doc, number, number)
            correctResponse.appendChild(createValue(doc, number, number))
            mapping.appendChild(createMapEntry(doc, number, number))



            mapMemberDeclaration(doc, number, number)
        }
        blockquote.appendChild(createBlockQuote(doc, code, number))
        number++
    }

    val gapMatchInteraction = doc.getElementsByTagName("gapMatchInteraction").item(0)
    gapMatchInteraction.appendChild(blockquote)
    responseDeclaration.appendChild(correctResponse)
    responseDeclaration.appendChild(mapping)


    var source = DOMSource(doc)
    return source
}

// GapText | The drag/droppable elements [splitCode = true]
fun createGapText(doc: Document, code: Array<String>, answerNumber: Int): Document {
    val gapMatchInteraction = doc.getElementsByTagName("gapMatchInteraction").item(0)
    val gapText = doc.createElement("gapText")
    gapText.setAttribute("identifier", "A$answerNumber")
    gapText.setAttribute("matchMax", "0")
    gapText.setAttribute("matchMin", "0")
    val span = doc.createElement("span")
    span.appendChild(doc.createTextNode(code[1]))
    gapText.appendChild(span)
    gapMatchInteraction.appendChild(gapText)

    return doc
}

// Blockquote | The surrounding text/code with GAPS (drop-fields) [splitCode = both]
fun createBlockQuote(doc: Document, code: Array<String>, gapNumber: Int): Element? {

    return if (code.size == 1) {
        val element = doc.createElement("p")
        element.appendChild(doc.createTextNode(code[0]))
        element
    } else {
        val element = doc.createElement("p")
        element.appendChild(doc.createTextNode(code[0]))
        val gap = doc.createElement("gap")
        gap.setAttribute("identifier", "GAP$gapNumber")
        element.appendChild(gap)
        element.appendChild(doc.createTextNode(code[2]))
        element

    }

}

// ResponseDeclaration | Mapping between item and corresponding correct drop area [splitCode = true]
fun mapResponseDeclaration(doc: Document, answerNumber: Int, gapNumber: Int): Document {
    val responseDeclaration: Node = doc.getElementsByTagName("responseDeclaration").item(0)

    val mapKey = "A$answerNumber GAP$gapNumber"

    // correctResponse --> values
    val correctResponse = doc.createElement("correctResponse")
    val value = doc.createElement("value")
    value.appendChild(doc.createTextNode(mapKey))
    correctResponse.appendChild(value)

    // mapping --> mapEntry
    val mapping = doc.createElement("mapping")
    mapping.setAttribute("defaultValue", "0")
    val mapEntry = doc.createElement("mapEntry")
    mapEntry.setAttribute("mapKey", mapKey)
    mapEntry.setAttribute("mappedValue", "1")
    mapping.appendChild(mapEntry)

    responseDeclaration.appendChild(correctResponse)
    responseDeclaration.appendChild(mapping)

    return doc
}

fun createValue(doc: Document, answerNumber: Int, gapNumber: Int): Element? {
    val mapKey = "A$answerNumber GAP$gapNumber"
    val value = doc.createElement("value")
    value.appendChild(doc.createTextNode(mapKey))
    return value
}

fun createMapEntry(doc: Document, answerNumber: Int, gapNumber: Int): Element? {
    val mapKey = "A$answerNumber GAP$gapNumber"
    val mapEntry = doc.createElement("mapEntry")
    mapEntry.setAttribute("mapKey", mapKey)
    mapEntry.setAttribute("mappedValue", "1")
    return mapEntry
}

// ResponseProcessing | Mapping member declaration [splitCode = true]
fun mapMemberDeclaration(doc: Document, answerNumber: Int, gapNumber: Int): Document {
    val responseProcessing = doc.getElementsByTagName("responseProcessing").item(0)
    val responseCondition = responseProcessing.childNodes.item(3).childNodes
    val and = responseCondition.item(3).childNodes.item(1)
    val or = responseCondition.item(5).childNodes.item(1)

    val member = doc.createElement("member")
    val mapKey = "A$answerNumber GAP$gapNumber"
    val baseValue = doc.createElement("baseValue")
    baseValue.setAttribute("baseType", "directedPair")
    baseValue.appendChild(doc.createTextNode(mapKey))
    member.appendChild(baseValue)
    val variable = doc.createElement("variable")
    variable.setAttribute("identifier", "RESPONSE")
    member.appendChild(variable)
    val member2 = member.cloneNode(true)
    and.appendChild(member2)
    or.appendChild(member)

    return doc
}
