package com.github.chr1stian.masterbackend

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource

fun buildManifest(taskName: String): DOMSource {
    val dbf = DocumentBuilderFactory.newInstance()
    val db = dbf.newDocumentBuilder()
    val doc = db.parse("files/imsmanifest_skeleton.xml")

    val resource = doc.getElementsByTagName("resource").item(0)

    val identifier = resource.attributes.getNamedItem("identifier")
    identifier.textContent = taskName
    val href = resource.attributes.getNamedItem("href")
    href.textContent = "$taskName.xml"

    val langstring = doc.getElementsByTagName("imsmd:langstring").item(2)
    langstring.removeChild(langstring.firstChild)
    langstring.appendChild(doc.createTextNode(taskName))

    val file = doc.getElementsByTagName("file").item(0)
    file.attributes.getNamedItem("href").textContent = "$taskName.xml"


    return DOMSource(doc)
}