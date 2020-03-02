package com.github.chr1stian.masterbackend

import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource

fun buildTask(task: Task): DOMSource {
    val dbf = DocumentBuilderFactory.newInstance()
    val db = dbf.newDocumentBuilder()
    val doc = db.parse("/Users/christiannyvoll/Documents/Master/xml/inline-gap-match/inline_gap_match.xml")
    var assessmentItem = doc.firstChild

    // GapText | The drag/droppable elements
    val gapMatchInteraction = doc.getElementsByTagName("gapMatchInteraction").item(0)
    // val gapText = gapMatchInteraction.item(0).childNodes.item(3)
    var gapNumber3: Int = 1
    for (item in task.splitCode) {
        if (item.size != 1) {
            val gapText = doc.createElement("gapText")
            gapText.setAttribute("identifier", "A$gapNumber3")
            gapText.setAttribute("matchMax", "0")
            gapText.setAttribute("matchMin", "0")
            val span = doc.createElement("span")
            span.appendChild(doc.createTextNode(item[1]))
            gapText.appendChild(span)


            // gapMatchInteraction.item(0).insertBefore(doc.getElementsByTagName("blockquote").item(0), gapText)
            gapMatchInteraction.appendChild(gapText)

            gapNumber3++
        }

    }


    // Blockquote | The surrounding text/code with GAPS (drop-fields)
    val blockquote = doc.createElement("blockquote")
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
    gapMatchInteraction.appendChild(blockquote)

    // ResponseDeclaration | Mapping between item and corresponding correct drop area
    val responseDeclaration: Node = doc.getElementsByTagName("responseDeclaration").item(0)
    val correctResponse = doc.createElement("correctResponse")
    val mapping = doc.createElement("mapping")
    mapping.setAttribute("defaultValue", "0")
    var gapNumber: Int = 1
    for (item in task.splitCode) {
        if (item.size != 1) {
            val mapKey = "A$gapNumber GAP$gapNumber"

            val value = doc.createElement("value")
            value.appendChild(doc.createTextNode(mapKey))
            correctResponse.appendChild(value)


            val mapEntry = doc.createElement("mapEntry")
            mapEntry.setAttribute("mapKey", mapKey)
            mapEntry.setAttribute("mappedValue", "1")
            mapping.appendChild(mapEntry)



            gapNumber++

        }
        responseDeclaration.appendChild(correctResponse)
        responseDeclaration.appendChild(mapping)

    }

    // ResponseProcessing | Mapping member declaration
    val responseProcessing = doc.getElementsByTagName("responseProcessing").item(0)
    val responseCondition = responseProcessing.childNodes.item(3).childNodes
    val and = responseCondition.item(3).childNodes.item(1)
    val or = responseCondition.item(5).childNodes.item(1)

    var gapNumber2: Int = 1
    for (item in task.splitCode) {
        if (item.size != 1) {
            val member = doc.createElement("member")
            val mapKey = "A$gapNumber2 GAP$gapNumber2"
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
            gapNumber2++
        }
    }


    var source = DOMSource(doc)
    return source
}