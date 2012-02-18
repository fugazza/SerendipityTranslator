/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.translationWindow;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import serendipitytranslator.mainWindow.Plugin;

/**
 *
 * @author Vláďa
 */
public class LangFile {

    public static final int LOCATION_PLUGINS = 1;
    public static final int LOCATIONS_TRANSLATED = 2;
    
    public static final int WINDOWS_BRAKE = 1;
    public static final int UNIX_BRAKE = 2;

    private static int lineBreakStyle = UNIX_BRAKE;

    private File file = null;
    private Plugin plugin;
    private String language;
    private HashMap<String,String> definitions = new HashMap<String,String>();
    private ArrayList<LangFileElement> fileStructure = new ArrayList<LangFileElement>();
    private int version = 1;
    private int subversion = -1;
    private String authorName = "";

    private static final int BETWEEN_STRING = 0;
    private static final int BETWEEN_ALIGNEMENT = 1;
    private int betweenType = BETWEEN_STRING;
    private String betweenString = ", ";
    private int betweenPadding = 0;

    public static String getFolderFromLocation(int location, Plugin plugin) {
        switch (location) {
            case LOCATIONS_TRANSLATED:
                return plugin.getTranslatedDirName();
            case LOCATION_PLUGINS:
            default:
                return plugin.getFolder();
        }        
    }
    
    public static File getFile(String folderPath, String pluginName, String language) {
        String fileName = "lang_"+language+".inc.php";
        if (pluginName.equals("system")) {
            fileName = "serendipity_lang_"+language+".inc.php";
        }
        //System.out.println(folderPath+"/"+fileName);
        return new File(folderPath+"/"+fileName);
    }

    public LangFile(Plugin plugin, String language) {
        this(LOCATION_PLUGINS, plugin, language);
    }

    public LangFile(int location, Plugin plugin, String language) {
        this(getFolderFromLocation(location, plugin),plugin, language);
    }

    public LangFile(String pathToPlugin, Plugin plugin, String language) {
        this.plugin = plugin;
        this.language = language;
        
        this.file = getFile(pathToPlugin,plugin.getName(), language);
        //System.out.println("plugin: " + pluginName);
        parseFile();
    }

    public static String getOldDownloadDirName(String pluginName) {
        return "plugins/" + pluginName;
    }    
    private void parseFile() {
        //System.out.println("Language file parsing: " + file.getName());
        if (file != null && file.exists()) {

            //System.out.println("we are parsing");
            try {
                definitions.clear();
                fileStructure.clear();

                Charset charset = getLanguageCharset();
                PhpParser pars = new PhpParser(new InputStreamReader(new FileInputStream(file),charset));
                String statement;
                String line;
                String key;
                String value;
                StringTokenizer st;
                boolean headerRead = false;
                int linesRead = 0;
                while (pars.hasMoreTokens()) {
                    statement = pars.nextToken();
                    //System.out.println("$"+statement+"$");
                    if (++linesRead > 10) {
                        headerRead = true;
                    }

                    // try to find a header
                    if (!headerRead && statement.trim().startsWith("/**")) {
                        StringTokenizer st2 = new StringTokenizer(statement, getLineBreak());
                        st2.nextToken();
                        //processing file header
                        while (st2.hasMoreTokens()) {
                            line = st2.nextToken();
                            if (line.contains("@version")) {
                               st = new StringTokenizer(line," :.,");
                               while (st.hasMoreTokens()) {
                                   String token = st.nextToken();
                                   try {
                                       version = Integer.parseInt(token);
                                       subversion = Integer.parseInt(st.nextToken());
                                       break;
                                   } catch (NumberFormatException e) {
                                       // continue without taking care
                                   }
                               }
                            } else if (!(line.contains("@author Translator Name <yourmail@example.com>")
                                         || line.contains("*/"))) {
                                fileStructure.add(new LangFileElement(LangFileElement.HEADER, line));
                            }
                        }
                        headerRead = true;
                    } else if (statement.trim().startsWith("/*") && headerRead) {
                        fileStructure.add(new LangFileElement(LangFileElement.STRING, statement));
                    } else if (statement.trim().startsWith("//") && headerRead) {
                        if (!statement.trim().equals("// Translate")) {
                            fileStructure.add(new LangFileElement(LangFileElement.STRING, statement));
                        }
                    } else if (statement.trim().startsWith("@define") || statement.trim().startsWith("define")) {
                        String prefix;
                        headerRead = true;
                        if (statement.trim().startsWith("@define")) {
                            prefix= statement.substring(0, statement.indexOf("@define"));
                        } else {
                            prefix= statement.substring(0, statement.indexOf("define"));
                        }
                        
                        if (prefix.isEmpty()) {
                            prefix = getLineBreak();
                        }

                        st = new StringTokenizer(statement.trim(),"'\"",true);
                        //System.out.println(st.countTokens()+ ": "+line);
                        if (st.countTokens() >=8) {
                            st.nextToken(); // first token is "@define("
                            st.nextToken(); // this is delimiter
                            key = st.nextToken();
//                            while(key.endsWith("\\")) {
//                                key += st.nextToken();
//                            }
                            st.nextToken(); // this is delimiter
                            String between = st.nextToken(); // this is between delimiters
                            value = st.nextToken(); // this is delimiter
                            //value += st.nextToken();
                            while(st.countTokens() > 2) {
                                value += st.nextToken();
                            }
                            value += st.nextToken(); // add trailing delimiter

                            value = makeLineEndsConsistent(value);

                            definitions.put(key, value);
                            fileStructure.add(new LangFileElement(LangFileElement.KEY, key, prefix, between));
                            //System.out.println(key + ": " + value);
                            //System.out.println(key + ": prefix = $" + prefix + "$");
                        }
                    } else if (headerRead &&
                            !statement.trim().startsWith("<?php") &&
                            !statement.trim().startsWith("?>")) {
                        if (!(statement.isEmpty())) {
                            fileStructure.add(new LangFileElement(LangFileElement.STATEMENT, statement));
                        } else {
                            fileStructure.add(new LangFileElement(LangFileElement.STRING, statement));
                        }
                    }
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(LangFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LangFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        checkBetweens();
//        for (LangFileElement lfe: fileStructure) {
//            System.out.println(lfe.getType() + ": " + lfe.getLine());
//        }
    }

    private String getLineBreak() {
        if (lineBreakStyle == LangFile.WINDOWS_BRAKE) {
            return "\r\n";
        } else {
            return "\n";            
        }
    }
    
    private void checkBetweens() {
        HashMap<String,Integer> betweens = new HashMap<String,Integer>();
        HashMap<Integer,Integer> padCounts = new HashMap<Integer,Integer>();

        String between;
        int len;
        int keyCount = 0;

        for (LangFileElement lfe: fileStructure) {
            if (lfe.getType() == LangFileElement.KEY) {
                keyCount++;
                between = lfe.getBetween();
                if (betweens.containsKey(between)) {
                    betweens.put(between,betweens.get(between).intValue()+1);
                } else {
                    betweens.put(between,1);
                }

                len = lfe.getPrefix().length() + 9 + lfe.getLine().length() + lfe.getBetween().length();
                if (padCounts.containsKey(len)) {
                    padCounts.put(len,padCounts.get(len)+1);
                } else {
                    padCounts.put(len,1);
                }
            }
        }

        for( Entry<Integer,Integer> e: padCounts.entrySet()) {
            if (e.getValue().intValue() >= keyCount/2) {
                betweenType = BETWEEN_ALIGNEMENT;
                betweenPadding = e.getKey().intValue();
                break;
            }
        }

        for( Entry<String,Integer> e: betweens.entrySet()) {
            if (e.getValue().intValue() >= keyCount/2) {
                betweenType = BETWEEN_STRING;
                betweenString = e.getKey();
                break;
            }
        }

        //System.out.println("between type = "+ betweenType + "; string = '" + betweenString + "'; pad count = " + betweenPadding);
    }

    private String makeBetween(LangFileElement lfe) {
        if (lfe.isNewElement()) {
            switch (betweenType) {
                case BETWEEN_ALIGNEMENT:
                    int len = lfe.getPrefix().length() + 9 + lfe.getLine().length();
                    String between = ",";
                    for (int i = len; i < betweenPadding; i++) {
                        between += " ";
                    }
                    return between;
                case BETWEEN_STRING:
                default:
                    return betweenString;
            }
        } else {
            return lfe.getBetween();
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<String>();
        for (LangFileElement lfe: fileStructure) {
            if (lfe.getType() == LangFileElement.KEY && !isSystemHandledKey(lfe.getLine())) {
                keys.add(lfe.getLine());
            }
        }
        return keys;
    }

    public void set(String key, String value) {
        if (!definitions.containsKey(key)) {
            fileStructure.add(new LangFileElement(LangFileElement.KEY,key,getLineBreak(),", ",true));
        }
        definitions.put(key, value);
    }

    public void addBlankLineToStructure() {
        fileStructure.add(new LangFileElement(LangFileElement.STRING,getLineBreak()));
    }

    public void addCommentToStructure(String comment) {
        fileStructure.add(new LangFileElement(LangFileElement.STRING,getLineBreak()+"// " + comment));
    }

    public String get(String key) {
        return definitions.get(key);
    }

    public int getKeysCount() {
        return definitions.size();
    }

    public boolean hasIdenticKeys(LangFile lang2) {
        ArrayList<String> keys1 = getKeys();
        ArrayList<String> keys2 = lang2.getKeys();

        for(String key: keys1) {
            if (!keys2.contains(key)) {
                return false;
            }
        }

        for(String key: keys2) {
            if (!keys1.contains(key)) {
                return false;
            }
        }

        return true;
    }

    public boolean isIdenticTo(LangFile lang2) {
        if (!hasIdenticKeys(lang2)) {
            //JOptionPane.showMessageDialog(null, pluginName + ": not identic keys");
            return false;
        }

        ArrayList<String> keys = getKeys();

        for(String key: keys) {
            if (!get(key).equals(lang2.get(key))) {
                //JOptionPane.showMessageDialog(null, pluginName + ": not identic message "+key);
                return false;
            }
        }

        return true;
    }

    public ArrayList<LangFileElement> getFileStructure() {
        return fileStructure;
    }

    public void setKeysStructure(ArrayList<LangFileElement> enFileStructure) {
        ArrayList<LangFileElement> elementsToRemove = new ArrayList<LangFileElement>();
        for (LangFileElement lfe: fileStructure) {
            if (lfe.getType() != LangFileElement.HEADER && lfe.getType() != LangFileElement.STATEMENT) {
                elementsToRemove.add(lfe);
            }
        }

        for (LangFileElement lfe: elementsToRemove) {
            fileStructure.remove(lfe);
        }

        for (LangFileElement lfe: enFileStructure) {
            if (lfe.getType() != LangFileElement.HEADER) {
                fileStructure.add(lfe);
                if (lfe.getType() == LangFileElement.KEY && !definitions.containsKey(lfe.getLine())) {
                    definitions.put(lfe.getLine(), "");
                }
            }
        }
    }

    private String makeLineEndsConsistent(String text) {
        if (lineBreakStyle == LangFile.WINDOWS_BRAKE) {
            text = text.replace("\n", "\r\n");
            text = text.replace("\r\r", "\r");
        } else {
            text = text.replace("\r\n", "\n");
        }        
        return text;
    }

    public void saveToFile() {
        if (language.equals("cs")) {
            language = "cz";
            __save();
            language = "cs";
        }
        __save();
    }

    private void __save() {

        String dirname = plugin.getTranslatedDirName();

        String UTFdirname = dirname + "/UTF-8";
        String fileName = "lang_" + language + ".inc.php";
        if (plugin.getName().equals("system")) {
            fileName = "serendipity_lang_"+language+".inc.php";
        }

        File UTFdir = new File(UTFdirname);
        if (!UTFdir.isDirectory()) {
            UTFdir.mkdirs();
        }
        StringWriter sw = new StringWriter();
        StringWriter swUTF = new StringWriter();

        try {
            SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            sw.write("<?php # lang_"+language+".inc.php "+version+"."+(subversion+1)+" "+longDateFormat.format(new Date())+" VladaAjgl $"+getLineBreak()+getLineBreak());
            sw.write("/**"+getLineBreak());
            sw.write(" *  @version " + version +"." + (subversion+1) + getLineBreak());
            for (LangFileElement lfe: fileStructure) {
                if (lfe.getType() == LangFileElement.HEADER) {
                    sw.write(lfe.getLine() + getLineBreak());
                }
            }
            sw.write(" *  @author "+authorName + getLineBreak());
            sw.write(" *  " + ((version > 1 || subversion >= 0) ? "@revisionDate" : "@translated") + " "+shortDateFormat.format(new Date()) + getLineBreak());
            sw.write(" */");
            swUTF.write(sw.toString()); // header is the same for local encoding and utf file
            for (LangFileElement lfe: fileStructure) {
                switch (lfe.getType()) {
                    case LangFileElement.KEY:
                        String key = lfe.getLine();
                        String prefix = lfe.getPrefix();
                        String between = lfe.getBetween();
                        String value = definitions.get(key);
                        if (plugin.getName().equals("system") && isSystemHandledKey(key)) {
                            //sw.write("@define('"+key+"',\t\t'"+getSystemKeyValue(key)+"');"+getLineBreak());
                            //swUTF.write("@define('"+key+"',\t\t'"+getUTFSystemKeyValue(key)+"');"+getLineBreak());
                            sw.write(prefix+"@define('"+key+"'"+makeBetween(lfe)+"'"+getSystemKeyValue(key)+"');");
                            swUTF.write(prefix+"@define('"+key+"'"+makeBetween(lfe)+"'"+getUTFSystemKeyValue(key)+"');");
                        } else if (value.trim().length() > 0) {
                            //String row = "@define('"+key+"',\t\t"+definitions.get(key)+");"+getLineBreak();
                            String row = prefix+"@define('"+key+"'"+makeBetween(lfe)+value+");";
                            sw.write(row);
                            swUTF.write(row);
                        }
                        break;
                    case LangFileElement.STRING:
                    case LangFileElement.STATEMENT:
                        String row = lfe.getLine();// + getLineBreak();
                        sw.write(row);
                        swUTF.write(row);
                        break;
                    default:
                        break;
                }
            }


            File localEncodingFile = new File(dirname + "/" + fileName);
            File translatedFile = new File(UTFdirname + "/" + fileName);

            // Create the encoder for charset
            Charset charset = getLanguageCharset();
            CharsetEncoder encoder = charset.newEncoder();

            // Convert a string to new charset bytes in a ByteBuffer
            // The new ByteBuffer is ready to be read.
            StringReader sr = new StringReader(makeLineEndsConsistent(sw.toString()));
            // do not rewrite repository file!!! - change from version 1.1
            //FileWriter repositoryFileWriter = new FileWriter(file);
            FileOutputStream fw = new FileOutputStream(localEncodingFile);

            CharBuffer cb = CharBuffer.allocate(1024);
            ByteBuffer bb;
            int bytesRead = 0;
            while ((bytesRead = sr.read(cb)) != -1) {
                cb.flip();
                //System.out.println("a: " + cb);
                bb = encoder.encode(cb);
                fw.write(bb.array(), 0, bb.limit());
                //cb.rewind();
                //repositoryFileWriter.write(cb.array(),0,bytesRead);
                cb.clear();
            }
            sr.close();
            fw.close();
            //repositoryFileWriter.close();

            // writing UTF-8 file
            CharsetEncoder UTFencoder = Charset.forName("UTF-8").newEncoder();
            FileOutputStream fwUTF = new FileOutputStream(translatedFile);
            StringReader srUTF = new StringReader(makeLineEndsConsistent(swUTF.toString()));
            CharBuffer cbUTF = CharBuffer.allocate(1024);
            while ((bytesRead = srUTF.read(cbUTF)) != -1) {
                cbUTF.flip();
                bb = UTFencoder.encode(cbUTF);
                fwUTF.write(bb.array(), 0, bb.limit());
                cbUTF.clear();
            }
            fwUTF.close();

            PhpParser.checkByExternalParser(localEncodingFile);
            PhpParser.checkByExternalParser(translatedFile);
        } catch (IOException ex) {
            if (ex.getMessage().startsWith("parser error:")) {
                JOptionPane.showMessageDialog(null, "There are errors in php code of saved file. Please check the file manually and remove the error.\r\n" + ex.getMessage(), "External php parser error", JOptionPane.ERROR_MESSAGE);
            } else {
                Logger.getLogger(LangFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String getCharsetName() {
        return getCharsets().get(language);
    }

    public static String getCharsetName (String lang) {
        return getCharsets().get(lang);
    }

    private Charset getLanguageCharset() {
        return Charset.forName(getCharsetName());
    }

    public String getVersion() {
        return version + "." + subversion;
    }

    public void update() {
        parseFile();
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    private boolean isSystemHandledKey(String key) {
        return (key.equals("LANG_CHARSET") || key.equals("SQL_CHARSET") || key.equals("DATE_LOCALES") || key.equals("WYSIWYG_LANG"));
    }

    private static HashMap<String,String> getCharsets() {
        HashMap<String,String> charsets = new HashMap<String,String>();
        charsets.put("bg", "windows-1251");
        charsets.put("cn", "UTF-8");
        charsets.put("cs", "windows-1250");
        charsets.put("cz", "ISO-8859-2");
        charsets.put("da", "ISO-8859-1");
        charsets.put("de", "ISO-8859-1");
        charsets.put("en", "ISO-8859-1");
        charsets.put("es", "ISO-8859-15");
        charsets.put("fa", "UTF-8");
        charsets.put("fi", "UTF-8");
        charsets.put("fr", "ISO-8859-1");
        charsets.put("hu", "ISO-8859-2");
        charsets.put("is", "ISO-8859-1");
        charsets.put("it", "ISO-8859-1");
        charsets.put("ja", "UTF-8");
        charsets.put("ko", "UTF-8");
        charsets.put("nl", "UTF-8");
        charsets.put("no", "ISO-8859-1");
        charsets.put("pl", "ISO-8859-2");
        charsets.put("pt", "ISO-8859-1");
        charsets.put("pt_PT", "ISO-8859-1");
        charsets.put("ro", "UTF-8");
        charsets.put("ru", "UTF-8");
        charsets.put("se", "ISO-8859-1");
        charsets.put("ta", "UTF-8");
        charsets.put("tn", "UTF-8");
        charsets.put("tr", "UTF-8");
        charsets.put("tw", "Big5");
        charsets.put("zh", "GB2312");
        return charsets;
    }

    private HashMap<String,String> getSqlCharsets() {
        HashMap<String,String> sqlCharsets = new HashMap<String,String>();
        sqlCharsets.put("bg", "cp1251");
        sqlCharsets.put("cn", "utf8");
        sqlCharsets.put("cs", "cp1250");
        sqlCharsets.put("cz", "latin2");
        sqlCharsets.put("da", "latin1");
        sqlCharsets.put("de", "latin1");
        sqlCharsets.put("en", "latin1");
        sqlCharsets.put("es", "latin2");
        sqlCharsets.put("fa", "utf8");
        sqlCharsets.put("fi", "utf8");
        sqlCharsets.put("fr", "latin1");
        sqlCharsets.put("hu", "latin2");
        sqlCharsets.put("is", "latin1");
        sqlCharsets.put("it", "latin1");
        sqlCharsets.put("ja", "utf8");
        sqlCharsets.put("ko", "utf8");
        sqlCharsets.put("nl", "utf8");
        sqlCharsets.put("no", "latin1");
        sqlCharsets.put("pl", "utf8");
        sqlCharsets.put("pt", "latin1");
        sqlCharsets.put("pt_PT", "latin1");
        sqlCharsets.put("ro", "utf8");
        sqlCharsets.put("ru", "utf8");
        sqlCharsets.put("se", "latin1");
        sqlCharsets.put("ta", "utf8");
        sqlCharsets.put("tn", "utf8");
        sqlCharsets.put("tr", "utf8");
        sqlCharsets.put("tw", "big5");
        sqlCharsets.put("zh", "gb2312");
        return sqlCharsets;
    }
    
    private HashMap<String,String> getDateLocales() {
        HashMap<String,String> dateLocales = new HashMap<String,String>();
        dateLocales.put("bg", "bulgarian, bg, bg_BG");
        dateLocales.put("cn", "zh_CN.UTF-8, cn, zh");
        dateLocales.put("cs", "cs_CZ.windows-1250, czech, cs");
        dateLocales.put("cz", "cs_CZ.ISO-8859-2, cs_CZ.ISO8859-2, czech, cs");
        dateLocales.put("da", "da_DK.ISO8859-1, da_DK.ISO-8859-1, danish, da, da_DK");
        dateLocales.put("de", "de_DE.ISO-8859-1, de_DE.ISO8859-1, german, de_DE, de_DE@euro, de");
        dateLocales.put("en", "en_US.ISO-8859-1, en_US.ISO8859-1, english, en, en_US");
        dateLocales.put("es", "es_ES.ISO8859-15, es_ES.ISO8859-1, spanish, sp, es, es_ES, es-ES, es_ES.ISO_8859-15, es_ES.ISO_8859-1");
        dateLocales.put("fa", "fa_IR.UTF-8, fa_IR, persian, fa");
        dateLocales.put("fi", "fi_FI.UTF-8, finnish, fi");
        dateLocales.put("fr", "fr_FR.ISO-8859-1, fr_FR.ISO8859-1, french, fr, fr_FR, fr_FR@euro, en_US");
        dateLocales.put("hu", "hu_HU.ISO-8859-2, hu_HU.ISO8859-2, hungarian, hu, hu_HU");
        dateLocales.put("is", "is_IS.ISO-8859-1, is_IS.ISO8859-1, icelandic, is, is_IS");
        dateLocales.put("it", "it_IT.ISO-8859-1, it_IT.ISO8859-1, italiano, it, it_IT");
        dateLocales.put("ja", "ja_JP.UTF-8,ja,jp");
        dateLocales.put("ko", "ko_KR.UTF-8, korean, ko, ko_KR");
        dateLocales.put("nl", "nl_BE.UTF8, nl_BE.UTF-8, dutch, nl_BE, nl");
        dateLocales.put("no", "no_NO.ISO-8859-1, no_NO.ISO8859-1, norwegian, no, no_NO, no_");
        dateLocales.put("pl", "pl.UTF-8, pl.UTF8, pl_PL.UTF-8, pl_PL.UTF8, polish, pl, pl_PL");
        dateLocales.put("pt", "pt_BR.ISO-8859-1, pt_BR.ISO8859-1, pt_BR, portuguese brazilian, pt");
        dateLocales.put("pt_PT", "pt_PT.ISO-8859-1, pt_PT.ISO8859-1, pt, pt_PT, european portuguese");
        dateLocales.put("ro", "ro_RO.UTF-8, romanian, ro, ro_RO");
        dateLocales.put("ru", "ru_RU.utf-8");
        dateLocales.put("se", "sv_SV.ISO8859-1, sv_SV.ISO-8859-1, swedish, sv, sv_SV, sv_SE.ISO8859-1, sv_SE.ISO-8859-1, sv_SE");
        dateLocales.put("ta", "ta_IN.UTF-8, en.UTF-8, en_US.UTF-8, english, en, en_US");
        dateLocales.put("tn", "zh-TW.UTF-8, zh_TW.UTF-8, tw, zh");
        dateLocales.put("tr", "tr_TR.UTF-8, tr.UTF-8, turkish.UTF-8, turkish, tr, tr_TR");
        dateLocales.put("tw", "tw, zh, zh-TW, zh_TW");
        dateLocales.put("zh", "zh_CN.GB2312, cn, zh, zh_GB, zh_CN");
        return dateLocales;
    }

    private HashMap<String,String> getWysiwygLang() {
        HashMap<String,String> wysiwygLang = new HashMap<String,String>();
        wysiwygLang.put("bg", "en");
        wysiwygLang.put("cn", "en");
        wysiwygLang.put("cs", "cs-win");
        wysiwygLang.put("cz", "cs-iso");
        wysiwygLang.put("da", "da");
        wysiwygLang.put("de", "de");
        wysiwygLang.put("en", "en");
        wysiwygLang.put("es", "es");
        wysiwygLang.put("fa", "en");
        wysiwygLang.put("fi", "fi");
        wysiwygLang.put("fr", "fr");
        wysiwygLang.put("hu", "en");
        wysiwygLang.put("is", "en");
        wysiwygLang.put("it", "it");
        wysiwygLang.put("ja", "ja-utf8");
        wysiwygLang.put("ko", "en");
        wysiwygLang.put("nl", "nl-utf");
        wysiwygLang.put("no", "no");
        wysiwygLang.put("pl", "en");
        wysiwygLang.put("pt", "pt_pt");
        wysiwygLang.put("pt_PT", "pt_pt");
        wysiwygLang.put("ro", "ro");
        wysiwygLang.put("ru", "en");
        wysiwygLang.put("se", "se");
        wysiwygLang.put("ta", "en");
        wysiwygLang.put("tn", "en");
        wysiwygLang.put("tr", "en");
        wysiwygLang.put("tw", "en");
        wysiwygLang.put("zh", "en");
        return wysiwygLang;
    }

    private HashMap<String,String> getUTFWysiwygLang() {
        HashMap<String,String> wysiwygLang = new HashMap<String,String>();
        wysiwygLang.put("bg", "en");
        wysiwygLang.put("cn", "en");
        wysiwygLang.put("cs", "cs-utf");
        wysiwygLang.put("cz", "cs-utf");
        wysiwygLang.put("da", "da-utf");
        wysiwygLang.put("de", "de-utf");
        wysiwygLang.put("en", "en");
        wysiwygLang.put("es", "es-utf");
        wysiwygLang.put("fa", "en");
        wysiwygLang.put("fi", "fi-utf");
        wysiwygLang.put("fr", "fr-utf");
        wysiwygLang.put("hu", "en");
        wysiwygLang.put("is", "en");
        wysiwygLang.put("it", "it");
        wysiwygLang.put("ja", "ja-utf8");
        wysiwygLang.put("ko", "en");
        wysiwygLang.put("nl", "nl-utf");
        wysiwygLang.put("no", "no-utf");
        wysiwygLang.put("pl", "en");
        wysiwygLang.put("pt", "pt_pt-utf");
        wysiwygLang.put("pt_PT", "pt_pt-utf");
        wysiwygLang.put("ro", "ro-utf");
        wysiwygLang.put("ru", "en");
        wysiwygLang.put("se", "se-utf");
        wysiwygLang.put("ta", "en");
        wysiwygLang.put("tn", "en");
        wysiwygLang.put("tr", "en");
        wysiwygLang.put("tw", "en");
        wysiwygLang.put("zh", "en");
        return wysiwygLang;
    }

    private HashMap<String,String> getUTFLocales() {
        HashMap<String,String> dateLocales = new HashMap<String,String>();
        dateLocales.put("bg", "bulgarian, bg, bg_BG");
        dateLocales.put("cn", "zh_CN.UTF-8, cn, zh");
        dateLocales.put("cs", "cs_CZ.UTF-8, czech, cs");
        dateLocales.put("cz", "cs_CZ.UTF-8, cs_CZ.UTF8, czech, cs");
        dateLocales.put("da", "da_DK.UTF8, da_DK.UTF-8, danish, da, da_DK");
        dateLocales.put("de", "de_DE.UTF-8, de_DE.UTF8, german, de_DE, de_DE@euro, de");
        dateLocales.put("en", "en_US.UTF-8, en_US.UTF8, english, en, en_US");
        dateLocales.put("es", "es_ES.UTF8, es_ES.ISO8859-1, spanish, sp, es, es_ES, es-ES, es_ES.ISO_8859-15, es_ES.ISO_8859-1");
        dateLocales.put("fa", "fa_IR.UTF-8, fa_IR, persian, fa");
        dateLocales.put("fi", "fi_FI.UTF-8, finnish, fi");
        dateLocales.put("fr", "fr_FR.UTF-8, fr_FR.UTF8, french, fr, fr_FR, fr_FR@euro, en_US");
        dateLocales.put("hu", "hu_HU.UTF-8, hu_HU.UTF8, hungarian, hu, hu_HU");
        dateLocales.put("is", "is_IS.UTF-8, is_IS.UTF8, icelandic, is, is_IS");
        dateLocales.put("it", "it_IT.UTF-8, it_IT.UTF8, italiano, it, it_IT");
        dateLocales.put("ja", "ja_JP.UTF-8,ja,jp");
        dateLocales.put("ko", "ko_KR.UTF-8, korean, ko, ko_KR");
        dateLocales.put("nl", "nl_BE.UTF8, nl_BE.UTF-8, dutch, nl_BE, nl");
        dateLocales.put("no", "no_NO.UTF-8, no_NO.UTF8, norwegian, no, no_NO, no_");
        dateLocales.put("pl", "pl.UTF-8, pl.UTF8, pl_PL.UTF-8, pl_PL.UTF8, olish, pl, pl_PL");
        dateLocales.put("pt", "pt_BR.UTF-8, pt_BR.UTF8, pt_BR, portuguese brazilian, pt");
        dateLocales.put("pt_PT", "pt_PT.UTF-8, pt_PT.UTF8, pt, pt_PT, european portuguese");
        dateLocales.put("ro", "ro_RO.UTF-8, romanian, ro, ro_RO");
        dateLocales.put("ru", "ru_RU.utf-8");
        dateLocales.put("se", "sv_SV.UTF8, sv_SV.UTF-8, swedish, sv, sv_SV, sv_SE.UTF8, sv_SE.UTF-8, sv_SE");
        dateLocales.put("ta", "ta_IN.UTF-8, en.UTF-8, en_US.UTF-8, english, en, en_U");
        dateLocales.put("tn", "zh-TW.UTF-8, zh_TW.UTF-8, tw, zh");
        dateLocales.put("tr", "tr_TR.UTF-8, tr.UTF-8, turkish.UTF-8, turkish, tr, tr_TR");
        dateLocales.put("tw", "tw, zh, zh-TW, zh_TW");
        dateLocales.put("zh", "zh_CN.UTF-8, cn, zh, zh_GB, zh_CN");
        return dateLocales;
    }

    private String getSystemKeyValue(String key) {
        if (key.equals("LANG_CHARSET")) {
            return getCharsetName();
        } else if (key.equals("SQL_CHARSET")) {
            return getSqlCharsets().get(language);
        } else if (key.equals("DATE_LOCALES")) {
            return getDateLocales().get(language);
        } else if (key.equals("WYSIWYG_LANG")) {
            return getWysiwygLang().get(language);
        } else {
            throw new IllegalArgumentException("Key must be one of system keys. Is: " + key);
        }

    }

    private String getUTFSystemKeyValue(String key) {
        if (key.equals("LANG_CHARSET")) {
            return "UTF-8";
        } else if (key.equals("SQL_CHARSET")) {
            return "utf8";
        } else if (key.equals("DATE_LOCALES")) {
            return getUTFLocales().get(language);
        } else if (key.equals("WYSIWYG_LANG")) {
            return getUTFWysiwygLang().get(language);
        } else {
            throw new IllegalArgumentException("Key must be one of system keys. Is: " + key);
        }

    }

    @Override
    public LangFile clone() {
        LangFile lf = new LangFile(plugin,language);
        lf.file = this.file;
        lf.plugin = this.plugin;
        lf.language = this.language;
        lf.definitions = (HashMap<String,String>) this.definitions.clone();
        lf.fileStructure = (ArrayList<LangFileElement>) this.fileStructure.clone();
        lf.version = this.version;
        lf.subversion = this.subversion;
        lf.authorName = this.authorName;

        return lf;
    }

    public static void setLineBreakStyle(int lineBreakStyle) {
        LangFile.lineBreakStyle = lineBreakStyle;
    }


}

