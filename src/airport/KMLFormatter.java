package airport;

//https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KMLFormatter {

	public KMLFormatter() {

	}

	public void createKML(Map<String, List<Double>> airports, String nameOfFile) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("kml");
			doc.appendChild(rootElement);

			// set attribute to kml element
			Attr attr = doc.createAttribute("xmlns");
			Attr attr1 = doc.createAttribute("xmlns:gx");
			Attr attr2 = doc.createAttribute("xmlns:kml");
			Attr attr3 = doc.createAttribute("xmlns:atom");
			attr.setValue("http://www.opengis.net/kml/2.2");
			attr1.setValue("http://www.google.com/kml/ext/2.2");
			attr2.setValue("http://www.opengis.net/kml/2.2");
			attr3.setValue("http://www.w3.org/2005/Atom");
			rootElement.setAttributeNode(attr);
			rootElement.setAttributeNode(attr1);
			rootElement.setAttributeNode(attr2);
			rootElement.setAttributeNode(attr3);

			// document element
			Element document = doc.createElement("Document");
			rootElement.appendChild(document);

			// shorten way
			// staff.setAttribute("id", "1");

			// name element
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(nameOfFile + ".kml")); // el nombre del archivo
			document.appendChild(name);

			// StyleMap element
			Element styleMap = doc.createElement("StyleMap");
			document.appendChild(styleMap);

			// set attribute to StyleMap element
			Attr attr9 = doc.createAttribute("id");
			attr9.setValue("m_ylw-pushpin");
			styleMap.setAttributeNode(attr9);

			// Pair element
			Element pair = doc.createElement("Pair");
			styleMap.appendChild(pair);

			// key element
			Element key = doc.createElement("key");
			key.appendChild(doc.createTextNode("normal"));
			pair.appendChild(key);

			// styleUrl element
			Element styleUrl = doc.createElement("styleUrl");
			styleUrl.appendChild(doc.createTextNode("#s_ylw-pushpin"));
			pair.appendChild(styleUrl);

			// Pair element
			Element pair2 = doc.createElement("Pair");
			styleMap.appendChild(pair2);

			// key element
			Element key2 = doc.createElement("key");
			key2.appendChild(doc.createTextNode("highlight"));
			pair2.appendChild(key2);

			// styleUrl element
			Element styleUrl2 = doc.createElement("styleUrl");
			styleUrl2.appendChild(doc.createTextNode("#s_ylw-pushpin_hl"));
			pair2.appendChild(styleUrl2);

			// Style element
			Element style = doc.createElement("Style");
			document.appendChild(style);

			// set attribute to Style element
			Attr attr4 = doc.createAttribute("id");
			attr4.setValue("s_ylw-pushpin_hl");
			style.setAttributeNode(attr4);

			// IconStyle element
			Element iconStyle = doc.createElement("IconStyle");
			style.appendChild(iconStyle);

			// scale element
			Element scale = doc.createElement("scale");
			scale.appendChild(doc.createTextNode("1.3"));
			iconStyle.appendChild(scale);

			// Icon element
			Element icon = doc.createElement("Icon");
			iconStyle.appendChild(icon);

			// href element
			Element href = doc.createElement("href");
			href.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png"));
			icon.appendChild(href);

			// hotSpot element
			Element hotSpot = doc.createElement("hotSpot");
			iconStyle.appendChild(hotSpot);

			// set attributes to hotSpot element
			Attr attr5 = doc.createAttribute("x");
			Attr attr6 = doc.createAttribute("y");
			Attr attr7 = doc.createAttribute("xunits");
			Attr attr8 = doc.createAttribute("yunits");
			attr5.setValue("20");
			attr6.setValue("2");
			attr7.setValue("pixels");
			attr8.setValue("pixels");
			hotSpot.setAttributeNode(attr5);
			hotSpot.setAttributeNode(attr6);
			hotSpot.setAttributeNode(attr7);
			hotSpot.setAttributeNode(attr8);

			// Style element
			Element style2 = doc.createElement("Style");
			document.appendChild(style2);

			// set attribute to Style element
			Attr attr10 = doc.createAttribute("id");
			attr10.setValue("s_ylw-pushpin");
			style2.setAttributeNode(attr10);

			// IconStyle element
			Element iconStyle2 = doc.createElement("IconStyle");
			style2.appendChild(iconStyle2);

			// scale element
			Element scale2 = doc.createElement("scale");
			scale2.appendChild(doc.createTextNode("1.1"));
			iconStyle2.appendChild(scale2);

			// Icon element
			Element icon2 = doc.createElement("Icon");
			iconStyle2.appendChild(icon2);

			// href element
			Element href2 = doc.createElement("href");
			href2.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png"));
			icon2.appendChild(href2);

			// hotSpot element
			Element hotSpot2 = doc.createElement("hotSpot");
			iconStyle2.appendChild(hotSpot2);

			// set attributes to hotSpot element
			Attr attr11 = doc.createAttribute("x");
			Attr attr12 = doc.createAttribute("y");
			Attr attr13 = doc.createAttribute("xunits");
			Attr attr14 = doc.createAttribute("yunits");
			attr11.setValue("20");
			attr12.setValue("2");
			attr13.setValue("pixels");
			attr14.setValue("pixels");
			hotSpot2.setAttributeNode(attr11);
			hotSpot2.setAttributeNode(attr12);
			hotSpot2.setAttributeNode(attr13);
			hotSpot2.setAttributeNode(attr14);

			for (Entry<String, List<Double>> airport : airports.entrySet()) {

				// Placemark element
				Element placemark = doc.createElement("Placemark");
				document.appendChild(placemark);

				// name element
				Element name2 = doc.createElement("name");
				name2.appendChild(doc.createTextNode(airport.getKey()));
				placemark.appendChild(name2);

				// LookAt element
				Element lookAt = doc.createElement("LookAt");
				placemark.appendChild(lookAt);

				// longitude element
				Element longitude2 = doc.createElement("longitude");
				longitude2.appendChild(doc.createTextNode(airport.getValue().get(1).toString()));
				lookAt.appendChild(longitude2);

				// latitude element
				Element latitude2 = doc.createElement("latitude");
				latitude2.appendChild(doc.createTextNode(airport.getValue().get(0).toString()));
				lookAt.appendChild(latitude2);

				// tilt element
				Element altitude = doc.createElement("altitude");
				altitude.appendChild(doc.createTextNode("0"));
				lookAt.appendChild(altitude);

				// heading element
				Element heading = doc.createElement("heading");
				heading.appendChild(doc.createTextNode("-0.0009426523021906111"));
				lookAt.appendChild(heading);

				// tilt element
				Element tilt = doc.createElement("tilt");
				tilt.appendChild(doc.createTextNode("0"));
				lookAt.appendChild(tilt);

				// range element
				Element range = doc.createElement("range");
				// el range es la distancia entre el mapa y el ojo, deberíamos calcularlo
				// dependiendo de la distancia entre los lugares que se guardan
				range.appendChild(doc.createTextNode("1020.448950673321"));
				lookAt.appendChild(range);

				// gx:altitudeMode element
				Element gxaltitudeMode = doc.createElement("gx:altitudeMode");
				gxaltitudeMode.appendChild(doc.createTextNode("relativeToSeaFloor")); // o relativeToSeaFloor1
				lookAt.appendChild(gxaltitudeMode);

				// styleUrl element
				Element styleUrl3 = doc.createElement("styleUrl");
				styleUrl3.appendChild(doc.createTextNode("#m_ylw-pushpin")); // podria ponerlo aca
																				// http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png
				placemark.appendChild(styleUrl3);

				// Point element
				Element point = doc.createElement("Point");
				placemark.appendChild(point);

				// gx:drawOrder element
				Element gxDrawOrder = doc.createElement("gx:drawOrder");
				gxDrawOrder.appendChild(doc.createTextNode("1"));
				point.appendChild(gxDrawOrder);

				// coordinates element
				Element coordinates = doc.createElement("coordinates");
				coordinates.appendChild(doc.createTextNode(
						airport.getValue().get(1).toString() + "," + airport.getValue().get(0).toString() + ",0"));
				point.appendChild(coordinates);
			}

			List<String> aux = new ArrayList<>();
			for (String airport : airports.keySet())
				aux.add(airport);

			for (int i = 0; i < aux.size() - 1; i++) {

				// Placemark element
				Element placemark = doc.createElement("Placemark");
				document.appendChild(placemark);

				// name element
				Element name2 = doc.createElement("name");
				name2.appendChild(doc.createTextNode(aux.get(i) + "-" + aux.get(i + 1)));
				placemark.appendChild(name2);

				// styleUrl element
				Element styleUrl3 = doc.createElement("styleUrl");
				styleUrl3.appendChild(doc.createTextNode("#m_ylw-pushpin"));
				placemark.appendChild(styleUrl3);

				// LineString element
				Element lineString = doc.createElement("LineString");
				placemark.appendChild(lineString);

				// tessellate element
				Element tessellate = doc.createElement("tessellate");
				tessellate.appendChild(doc.createTextNode("1"));
				lineString.appendChild(tessellate);

				// coordinates element
				Element coordinates = doc.createElement("coordinates");
				coordinates.appendChild(doc.createTextNode(
						airports.get(aux.get(i)).get(1).toString() + "," + airports.get(aux.get(i)).get(0).toString()
								+ ",0 " + airports.get(aux.get(i + 1)).get(1).toString() + ","
								+ airports.get(aux.get(i + 1)).get(0).toString() + ",0"));
				lineString.appendChild(coordinates);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			if (nameOfFile.equals("stdout")) {
				StreamResult result = new StreamResult(System.out);
				transformer.transform(source, result);

			} else {
				StreamResult result = new StreamResult(new File("./" + nameOfFile + ".kml")); // WINDOWS ".\\" + nameOfFile + ".kml"
				transformer.transform(source, result);
				System.out.println("File saved!");
			}

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}
