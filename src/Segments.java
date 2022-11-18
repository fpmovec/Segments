import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class Segments {

    private static TreeMap<String, SegmentInfo> _segmentsTreeMap;
    public static void main(String[] argv) throws ParserConfigurationException, IOException, SAXException, XMLStreamException, ParseException {
        _segmentsTreeMap = new TreeMap<>();
          //double[] pointsValues = XmlRead("E:\\javaXML.xml");
         double[] pointsValues = JsonRead("E:\\JSONjava.json");
       boolean isIntersect = WhatDoesSheDo(pointsValues, _segmentsTreeMap);
       Iterator itr = _segmentsTreeMap.entrySet().iterator();
      //  _segmentsTreeMap.forEach((k, v) -> System.out.println("Key: " + k + "Value: " + v));
        System.out.println();
        for (Map.Entry<String, SegmentInfo> pair : _segmentsTreeMap.entrySet()) {
            System.out.println("Key: " + pair.getKey() + " Value: " + pair.getValue());
        }
       System.out.println(isIntersect);
       ZIPWriter(XMLWriter(isIntersect), "xmlArchive");
       JarWriter(XMLWriter(isIntersect), "JarArchive");
       JsonWriter(isIntersect);
    }
 public static double[] JsonRead(String path) throws IOException, ParseException {
       double[] array = new double[8];
       int count = 0;
        Object obj = new JSONParser().parse(new FileReader(path));
        JSONObject jo = (JSONObject) obj;
        array[0] = Double.parseDouble((String) jo.get("pointOneX"));
     array[1] = Double.parseDouble((String) jo.get("pointOneY"));
     array[2] = Double.parseDouble((String) jo.get("pointTwoX"));
     array[3] = Double.parseDouble((String) jo.get("pointTwoY"));
     array[4] = Double.parseDouble((String) jo.get("pointOneX1"));
     array[5] = Double.parseDouble((String) jo.get("pointOneY1"));
     array[6] = Double.parseDouble((String) jo.get("pointTwoX1"));
     array[7] = Double.parseDouble((String) jo.get("pointTwoY1"));
        return array;
 }
    public static String JsonWriter(boolean isIntersect) throws IOException {
        JSONObject json = new JSONObject();
        json.put("IsIntersect", isIntersect);
        FileWriter out = new FileWriter("E:\\JSONResult.json");
        out.write(json.toJSONString());
        out.close();
        return "E:\\JSONResult.json";
    }

    public static String XMLWriter(boolean isIntersect) throws IOException, XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        String fileName = "E:\\XMLResult.xml";
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter(fileName));
        writer.writeStartDocument("1.0");
        writer.writeStartElement("intersect");
           writer.writeCharacters(Boolean.toString(isIntersect));
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();

        return fileName;
    }

    public static void JarWriter(String filePath, String name) throws IOException {
        JarOutputStream zipOutputStream = new JarOutputStream(new FileOutputStream("E:\\" + name + ".jar"));
        FileInputStream fis = new FileInputStream(filePath);
        JarEntry zipEntry = new JarEntry("Result.xml");
        zipOutputStream.putNextEntry(zipEntry);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        zipOutputStream.write(buffer);
        fis.close();
        zipOutputStream.close();
    }
    public static void ZIPWriter(String filePath, String name) throws IOException
    {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("E:\\" + name + ".zip"));
        FileInputStream fis = new FileInputStream(filePath);
        ZipEntry zipEntry = new ZipEntry("Result.xml");
        zipOutputStream.putNextEntry(zipEntry);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        zipOutputStream.write(buffer);
        fis.close();
        zipOutputStream.close();
    }
 public static boolean WhatDoesSheDo(double[] valuesArray, TreeMap<String, SegmentInfo> _segmentsTreeMap)
 {
     SegmentInfo firstSegmentFirstPoint = new SegmentInfo(valuesArray[0], valuesArray[1]);
     SegmentInfo firstSegmentSecondPoint = new SegmentInfo(valuesArray[2], valuesArray[3]);
     SegmentInfo secondSegmentFirstPoint = new SegmentInfo(valuesArray[4], valuesArray[5]);
     SegmentInfo secondSegmentSecondPoint = new SegmentInfo(valuesArray[6], valuesArray[7]);

     _segmentsTreeMap.put("fSfP", firstSegmentFirstPoint);
     _segmentsTreeMap.put("fSsP", firstSegmentSecondPoint);
     _segmentsTreeMap.put("sSfP", secondSegmentFirstPoint);
     _segmentsTreeMap.put("sSsP", secondSegmentSecondPoint);
      return IsIntersect(_segmentsTreeMap.get("fSfP"),
              _segmentsTreeMap.get("fSsP"),
              _segmentsTreeMap.get("sSfP"),
              _segmentsTreeMap.get("sSsP"));
 }
    private static double[] XmlRead(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(path);
        Node root = document.getDocumentElement();
       double[] returningArray = new double[8];
       int count = 0;
        NodeList segments = root.getChildNodes();
        for (int i = 0; i < segments.getLength(); i++){
            Node segment = segments.item(i);
            if (segment.getNodeType() != Node.TEXT_NODE){
                NodeList segmentPoints = segment.getChildNodes();
                for (int j = 0; j < segmentPoints.getLength(); j++){
                    Node segmentPoint = segmentPoints.item(j);
                    if (segmentPoint.getNodeType() != Node.TEXT_NODE)
                        returningArray[count++] = Double.parseDouble(segmentPoint.getChildNodes().item(0).getTextContent());
                       // System.out.println(segmentPoint.getNodeName()
                       // + ":" + segmentPoint.getChildNodes().item(0).getTextContent());
                }
            }
        }
        return returningArray;
    }
    private static boolean IsIntersect(SegmentInfo p1, SegmentInfo p2, SegmentInfo p3, SegmentInfo p4)
    {
        if (p2.GetX() < p1.GetX()) {

            SegmentInfo tmp = p1;
            p1 = p2;
            p2 = tmp;
        }

        if (p4.GetX() < p3.GetX()) {

            SegmentInfo tmp = p3;
            p3 = p4;
            p4 = tmp;
        }
        if (p2.GetX()< p3.GetX()) {
            return false;
        }

        //если оба отрезка вертикальные
        if((p1.GetX() - p2.GetX() == 0) && (p3.GetX() - p4.GetX() == 0)) {

            //если они лежат на одном X
            if(p1.GetX() == p3.GetX()) {

                //проверим пересекаются ли они, т.е. есть ли у них общий Y
                //для этого возьмём отрицание от случая, когда они НЕ пересекаются
                if (!((Math.max(p1.GetY(), p2.GetY()) < Math.min(p3.GetY(), p4.GetY())) ||
                        (Math.min(p1.GetY(), p2.GetY()) > Math.max(p3.GetY(), p4.GetY())))) {

                    return true;
                }
            }

            return false;
        }
        //если первый отрезок вертикальный
        if (p1.GetX() - p2.GetX() == 0) {

            //найдём Xa, Ya - точки пересечения двух прямых
            double Xa = p1.GetX();
            double A2 = (p3.GetY() - p4.GetX()) / (p3.GetX() - p4.GetX());
            double b2 = p3.GetY() - A2 * p3.GetX();
            double Ya = A2 * Xa + b2;

            if (p3.GetX() <= Xa && p4.GetX() >= Xa && Math.min(p1.GetY(), p2.GetY()) <= Ya &&
                    Math.max(p1.GetY(), p2.GetY()) >= Ya) {
                return true;
            }

            return false;
        }
        //если второй отрезок вертикальный
        if (p3.GetX() - p4.GetX() == 0) {

            //найдём Xa, Ya - точки пересечения двух прямых
            double Xa = p3.GetX();
            double A1 = (p1.GetY() - p2.GetY()) / (p1.GetX() - p2.GetX());
            double b1 = p1.GetY() - A1 * p1.GetX();
            double Ya = A1 * Xa + b1;

            if (p1.GetX() <= Xa && p2.GetX() >= Xa && Math.min(p3.GetY(), p4.GetY()) <= Ya &&
                    Math.max(p3.GetY(), p4.GetY()) >= Ya) {

                return true;
            }

            return false;
        }
        //оба отрезка невертикальные
        double A1 = (p1.GetY() - p2.GetY()) / (p1.GetX() - p2.GetX());
        double A2 = (p3.GetY() - p4.GetY()) / (p3.GetX() - p4.GetX());
        double b1 = p1.GetY() - A1 * p1.GetX();
        double b2 = p3.GetY() - A2 * p3.GetX();

        if (A1 == A2) {
            return false; //отрезки параллельны
        }

//Xa - абсцисса точки пересечения двух прямых
        double Xa = (b2 - b1) / (A1 - A2);

        if ((Xa < Math.max(p1.GetX(), p3.GetX())) || (Xa > Math.min( p2.GetX(), p4.GetX()))) {
            return false; //точка Xa находится вне пересечения проекций отрезков на ось X
        }
        else {
            return true;
        }
    }
}

