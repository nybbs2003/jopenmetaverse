package com.ngt.jopenmetaverse.shared.structureddata.llsd;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.validation.Schema;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.util.Utils;

/// </summary>
    public class XmlLLSDOSDParser
    {
//        private static Schema XmlSchema;
//        private static DocumentBuilder XmlTextReader;
//        private static String LastXmlErrors = "";
//        private static Object XmlValidationLock = new Object();
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="xmlData"></param>
//        /// <returns></returns>
//        public static OSD DeserializeLLSDXml(byte[] xmlData)
//        {
//            return DeserializeLLSDXml(new XmlTextReader(new ByteArrayInputStream(xmlData)));
//        }
//
//        public static OSD DeserializeLLSDXml(Stream xmlStream)
//        {
//            return DeserializeLLSDXml(new XmlTextReader(xmlStream));
//        }
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="xmlData"></param>
//        /// <returns></returns>
//        public static OSD DeserializeLLSDXml(String xmlData)
//        {
//            byte[] bytes = Utils.stringToBytes(xmlData);
//            return DeserializeLLSDXml(new XmlTextReader(new MemoryStream(bytes, false)));
//        }
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="xmlData"></param>
//        /// <returns></returns>
//        public static OSD DeserializeLLSDXml(XmlTextReader xmlData)
//        {
//            try
//            {
//                xmlData.Read();
//                SkipWhitespace(xmlData);
//
//                xmlData.Read();
//                OSD ret = ParseLLSDXmlElement(xmlData);
//
//                return ret;
//            }
//            catch
//            {
//                return new OSD();
//            }
//        }
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="data"></param>
//        /// <returns></returns>
//        public static byte[] SerializeLLSDXmlBytes(OSD data)
//        {
//            return Encoding.UTF8.GetBytes(SerializeLLSDXmlString(data));
//        }
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="data"></param>
//        /// <returns></returns>
//        public static String SerializeLLSDXmlString(OSD data)
//        {
//            StringWriter sw = new StringWriter();
//            XmlTextWriter writer = new XmlTextWriter(sw);
//            writer.Formatting = Formatting.None;
//
//            writer.WriteStartElement("", "llsd", "");
//            SerializeLLSDXmlElement(writer, data);
//            writer.WriteEndElement();
//
//            writer.Close();
//
//            return sw.ToString();
//        }
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="writer"></param>
//        /// <param name="data"></param>
//        public static void SerializeLLSDXmlElement(XmlTextWriter writer, OSD data)
//        {
//            switch (data.Type)
//            {
//                case OSDType.Unknown:
//                    writer.WriteStartElement("", "undef", "");
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.Boolean:
//                    writer.WriteStartElement("", "boolean", "");
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.Integer:
//                    writer.WriteStartElement("", "integer", "");
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.Real:
//                    writer.WriteStartElement("", "real", "");
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.String:
//                    writer.WriteStartElement("", "String", "");
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.UUID:
//                    writer.WriteStartElement("", "uuid", "");
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.Date:
//                    writer.WriteStartElement("", "date", "");
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.URI:
//                    writer.WriteStartElement("", "uri", "");
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.Binary:
//                    writer.WriteStartElement("", "binary", "");
//                        writer.WriteStartAttribute("", "encoding", "");
//                        writer.WriteString("base64");
//                        writer.WriteEndAttribute();
//                    writer.WriteString(data.asString());
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.Map:
//                    OSDMap map = (OSDMap)data;
//                    writer.WriteStartElement("", "map", "");
//                    foreach (KeyValuePair<String, OSD> kvp in map)
//                    {
//                        writer.WriteStartElement("", "key", "");
//                        writer.WriteString(kvp.Key);
//                        writer.WriteEndElement();
//
//                        SerializeLLSDXmlElement(writer, kvp.Value);
//                    }
//                    writer.WriteEndElement();
//                    break;
//                case OSDType.Array:
//                    OSDArray array = (OSDArray)data;
//                    writer.WriteStartElement("", "array", "");
//                    for (int i = 0; i < array.Count; i++)
//                    {
//                        SerializeLLSDXmlElement(writer, array[i]);
//                    }
//                    writer.WriteEndElement();
//                    break;
//            }
//        }
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="xmlData"></param>
//        /// <param name="error"></param>
//        /// <returns></returns>
//        public static boolean TryValidateLLSDXml(XmlTextReader xmlData, out String error)
//        {
//            lock (XmlValidationLock)
//            {
//                LastXmlErrors = "";
//                XmlTextReader = xmlData;
//
//                CreateLLSDXmlSchema();
//
//                XmlReaderSettings readerSettings = new XmlReaderSettings();
//                readerSettings.ValidationType = ValidationType.Schema;
//                readerSettings.Schemas.Add(XmlSchema);
//                readerSettings.ValidationEventHandler += new ValidationEventHandler(LLSDXmlSchemaValidationHandler);
//
//                XmlReader reader = XmlReader.Create(xmlData, readerSettings);
//
//                try
//                {
//                    while (reader.Read()) { }
//                }
//                catch (XmlException)
//                {
//                    error = LastXmlErrors;
//                    return false;
//                }
//
//                if (LastXmlErrors == "")
//                {
//                    error = null;
//                    return true;
//                }
//                else
//                {
//                    error = LastXmlErrors;
//                    return false;
//                }
//            }
//        }
//
//        /// <summary>
//        /// 
//        /// </summary>
//        /// <param name="reader"></param>
//        /// <returns></returns>
//        private static OSD ParseLLSDXmlElement(XmlTextReader reader)
//        {
//            SkipWhitespace(reader);
//
//            if (reader.NodeType != XmlNodeType.Element)
//                throw new OSDException("Expected an element");
//
//            String type = reader.LocalName;
//            OSD ret;
//
//            switch (type)
//            {
//                case "undef":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return new OSD();
//                    }
//
//                    reader.Read();
//                    SkipWhitespace(reader);
//                    ret = new OSD();
//                    break;
//                case "boolean":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromBoolean(false);
//                    }
//
//                    if (reader.Read())
//                    {
//                        String s = reader.ReadString().Trim();
//
//                        if (!String.IsNullOrEmpty(s) && (s == "true" || s == "1"))
//                        {
//                            ret = OSD.FromBoolean(true);
//                            break;
//                        }
//                    }
//
//                    ret = OSD.FromBoolean(false);
//                    break;
//                case "integer":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromInteger(0);
//                    }
//
//                    if (reader.Read())
//                    {
//                        int value = 0;
//                        Int32.TryParse(reader.ReadString().Trim(), out value);
//                        ret = OSD.FromInteger(value);
//                        break;
//                    }
//
//                    ret = OSD.FromInteger(0);
//                    break;
//                case "real":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromReal(0d);
//                    }
//
//                    if (reader.Read())
//                    {
//                        double value = 0d;
//                        String str = reader.ReadString().Trim().ToLower();
//
//                        if (str == "nan")
//                            value = Double.NaN;
//                        else
//                            Utils.TryParseDouble(str, out value);
//
//                        ret = OSD.FromReal(value);
//                        break;
//                    }
//
//                    ret = OSD.FromReal(0d);
//                    break;
//                case "uuid":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromUUID(UUID.Zero);
//                    }
//
//                    if (reader.Read())
//                    {
//                        UUID value = UUID.Zero;
//                        UUID.TryParse(reader.ReadString().Trim(), out value);
//                        ret = OSD.FromUUID(value);
//                        break;
//                    }
//
//                    ret = OSD.FromUUID(UUID.Zero);
//                    break;
//                case "date":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromDate(Utils.Epoch);
//                    }
//
//                    if (reader.Read())
//                    {
//                        DateTime value = Utils.Epoch;
//                        DateTime.TryParse(reader.ReadString().Trim(), out value);
//                        ret = OSD.FromDate(value);
//                        break;
//                    }
//
//                    ret = OSD.FromDate(Utils.Epoch);
//                    break;
//                case "String":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromString("");
//                    }
//
//                    if (reader.Read())
//                    {
//                        ret = OSD.FromString(reader.ReadString());
//                        break;
//                    }
//
//                    ret = OSD.FromString("");
//                    break;
//                case "binary":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromBinary(Utils.EmptyBytes);
//                    }
//
//                    if (reader.GetAttribute("encoding") != null && reader.GetAttribute("encoding") != "base64")
//                        throw new OSDException("Unsupported binary encoding: " + reader.GetAttribute("encoding"));
//
//                    if (reader.Read())
//                    {
//                        try
//                        {
//                            ret = OSD.FromBinary(Convert.FromBase64String(reader.ReadString().Trim()));
//                            break;
//                        }
//                        catch (FormatException ex)
//                        {
//                            throw new OSDException("Binary decoding exception: " + ex.Message);
//                        }
//                    }
//
//                    ret = OSD.FromBinary(Utils.EmptyBytes);
//                    break;
//                case "uri":
//                    if (reader.IsEmptyElement)
//                    {
//                        reader.Read();
//                        return OSD.FromUri(new Uri("", UriKind.RelativeOrAbsolute));
//                    }
//
//                    if (reader.Read())
//                    {
//                        ret = OSD.FromUri(new Uri(reader.ReadString(), UriKind.RelativeOrAbsolute));
//                        break;
//                    }
//
//                    ret = OSD.FromUri(new Uri("", UriKind.RelativeOrAbsolute));
//                    break;
//                case "map":
//                    return ParseLLSDXmlMap(reader);
//                case "array":
//                    return ParseLLSDXmlArray(reader);
//                default:
//                    reader.Read();
//                    ret = null;
//                    break;
//            }
//
//            if (reader.NodeType != XmlNodeType.EndElement || reader.LocalName != type)
//            {
//                throw new OSDException("Expected </" + type + ">");
//            }
//            else
//            {
//                reader.Read();
//                return ret;
//            }
//        }
//
//        private static OSDMap ParseLLSDXmlMap(XmlTextReader reader)
//        {
//            if (reader.NodeType != XmlNodeType.Element || reader.LocalName != "map")
//                throw new NotImplementedException("Expected <map>");
//
//            OSDMap map = new OSDMap();
//
//            if (reader.IsEmptyElement)
//            {
//                reader.Read();
//                return map;
//            }
//
//            if (reader.Read())
//            {
//                while (true)
//                {
//                    SkipWhitespace(reader);
//
//                    if (reader.NodeType == XmlNodeType.EndElement && reader.LocalName == "map")
//                    {
//                        reader.Read();
//                        break;
//                    }
//
//                    if (reader.NodeType != XmlNodeType.Element || reader.LocalName != "key")
//                        throw new OSDException("Expected <key>");
//
//                    String key = reader.ReadString();
//
//                    if (reader.NodeType != XmlNodeType.EndElement || reader.LocalName != "key")
//                        throw new OSDException("Expected </key>");
//
//                    if (reader.Read())
//                        map[key] = ParseLLSDXmlElement(reader);
//                    else
//                        throw new OSDException("Failed to parse a value for key " + key);
//                }
//            }
//
//            return map;
//        }
//
//        private static OSDArray ParseLLSDXmlArray(XmlTextReader reader)
//        {
//            if (reader.NodeType != XmlNodeType.Element || reader.LocalName != "array")
//                throw new OSDException("Expected <array>");
//
//            OSDArray array = new OSDArray();
//
//            if (reader.IsEmptyElement)
//            {
//                reader.Read();
//                return array;
//            }
//
//            if (reader.Read())
//            {
//                while (true)
//                {
//                    SkipWhitespace(reader);
//
//                    if (reader.NodeType == XmlNodeType.EndElement && reader.LocalName == "array")
//                    {
//                        reader.Read();
//                        break;
//                    }
//
//                    array.Add(ParseLLSDXmlElement(reader));
//                }
//            }
//
//            return array;
//        }        
//
//        private static void SkipWhitespace(XmlTextReader reader)
//        {
//            while (
//                reader.NodeType == XmlNodeType.Comment ||
//                reader.NodeType == XmlNodeType.Whitespace ||
//                reader.NodeType == XmlNodeType.SignificantWhitespace ||
//                reader.NodeType == XmlNodeType.XmlDeclaration)
//            {
//                reader.Read();
//            }
//        }
//
//        private static void CreateLLSDXmlSchema()
//        {
//            if (XmlSchema == null)
//            {
//                //region XSD
//                String schemaText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + 
//"<xs:schema elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">" + 
//  "<xs:import schemaLocation=\"xml.xsd\" namespace=\"http://www.w3.org/XML/1998/namespace\" />" + 
//  "<xs:element name=\"uri\" type=\"xs:String\" />" + 
//  "<xs:element name=\"uuid\" type=\"xs:String\" />" + 
//  "<xs:element name=\"KEYDATA\">"  + 
//    "<xs:complexType>" + 
//      "<xs:sequence>" + 
//        "<xs:element ref=\"key\" />" + 
//        "<xs:element ref=\"DATA\" />" + 
//      "</xs:sequence>" + 
//    "</xs:complexType>" +
//  "</xs:element>" + 
//  "<xs:element name=\"date\" type=\"xs:String\" />" + 
//  "<xs:element name=\"key\" type=\"xs:String\" />" + 
//  "<xs:element name=\"boolean\" type=\"xs:String\" />" + 
//  "<xs:element name=\"undef\">" + 
//    "<xs:complexType>" + 
//      "<xs:sequence>" + 
//        "<xs:element ref=\"EMPTY\" />" + 
//      "</xs:sequence>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//  "<xs:element name=\"map\">" + 
//    "<xs:complexType>" + 
//      "<xs:sequence>" + 
//        "<xs:element minOccurs=\"0\" maxOccurs=\"unbounded\" ref=\"KEYDATA\" />" + 
//      "</xs:sequence>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//  "<xs:element name=\"real\" type=\"xs:String\" />" + 
//  "<xs:element name=\"ATOMIC\">" + 
//    "<xs:complexType>" + 
//      "<xs:choice>" + 
//        "<xs:element ref=\"undef\" />" + 
//        "<xs:element ref=\"boolean\" />" + 
//        "<xs:element ref=\"integer\" />" + 
//        "<xs:element ref=\"real\" />" + 
//        "<xs:element ref=\"uuid\" />" + 
//        "<xs:element ref=\"String\" />" + 
//        "<xs:element ref=\"date\" />" + 
//        "<xs:element ref=\"uri\" />" + 
//        "<xs:element ref=\"binary\" />" + 
//      "</xs:choice>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//  "<xs:element name=\"DATA\">" + 
//    "<xs:complexType>" + 
//      "<xs:choice>" + 
//        "<xs:element ref=\"ATOMIC\" />" + 
//        "<xs:element ref=\"map\" />" + 
//        "<xs:element ref=\"array\" />" + 
//      "</xs:choice>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//  "<xs:element name=\"llsd\">" + 
//    "<xs:complexType>" + 
//      "<xs:sequence>" + 
//        "<xs:element ref=\"DATA\" />" + 
//      "</xs:sequence>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//  "<xs:element name=\"binary\">" + 
//    "<xs:complexType>" + 
//      "<xs:simpleContent>" + 
//        "<xs:extension base=\"xs:String\">" + 
//          "<xs:attribute default=\"base64\" name=\"encoding\" type=\"xs:String\" />"+
//        "</xs:extension>" + 
//      "</xs:simpleContent>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//  "<xs:element name=\"array\">" + 
//    "<xs:complexType>" + 
//      "<xs:sequence>" + 
//        "<xs:element minOccurs=\"0\" maxOccurs=\"unbounded\" ref=\"DATA\" />" + 
//      "</xs:sequence>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//  "<xs:element name=\"integer\" type=\"xs:String\" />" + 
//  "<xs:element name=\"String\">" + 
//    "<xs:complexType>" + 
//      "<xs:simpleContent>" + 
//        "<xs:extension base=\"xs:String\">" + 
//          "<xs:attribute ref=\"xml:space\" />" + 
//        "</xs:extension>" + 
//      "</xs:simpleContent>" + 
//    "</xs:complexType>" + 
//  "</xs:element>" + 
//"</xs:schema>" 
//  ;
//                //endregion XSD
//
//                MemoryStream stream = new MemoryStream(Encoding.ASCII.GetBytes(schemaText));
//
//                XmlSchema = new XmlSchema();
//                XmlSchema = XmlSchema.Read(stream, new ValidationEventHandler(LLSDXmlSchemaValidationHandler));
//            }
//        }
//
//        private static void LLSDXmlSchemaValidationHandler(Object sender, ValidationEventArgs args)
//        {
//            String error = sprintf(error, "Line: {0} - Position: {1} - {2}", XmlTextReader.LineNumber, XmlTextReader.LinePosition,
//                args.Message);
//
//            if (LastXmlErrors == "")
//                LastXmlErrors = error;
//            else
//                LastXmlErrors += Environment.NewLine + error;
//        }
//    }
}
