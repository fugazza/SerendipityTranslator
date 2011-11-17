/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serendipitytranslator.translationWindow;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;

/**
 *
 * @author Vláďa
 */
public class PhpParser implements Enumeration<String> {

    private Reader reader;
    private String token = null;
    private boolean inlineComment = false;
    private boolean comment = false;

    public static boolean hasClosedStrings(String statement) {
        StringReader sr = new StringReader(statement);
        int c;
        try {
            while ((c = sr.read()) != -1) {
                //System.out.println(Character.toChars(c).toString());
                    // string constant quoted by "
                    if (c == '"') {
                        c = sr.read();
                        while ((!(c == '"' || c == -1))) {
                            if (c == '\\') {
                                c = sr.read();
                                if (c == '"') {
                                    c = sr.read();
                                }
                            } else {
                                c = sr.read();
                            }
                        }
                        if (c == -1) {
                            return false;
                        }
                    }

                    // string constant quoted by '
                    else if (c == '\'') {
                        c = sr.read();
                        while ((!(c == '\'' || c == -1))) {
                            if (c == '\\') {
                                c = sr.read();
                                if (c == '\'') {
                                    c = sr.read();
                                }
                            } else {
                                c = sr.read();
                            }
                        }
                        if (c == -1) {
                            return false;
                        }
                    }
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public PhpParser(Reader r) {
        reader = r;
        findPhp();
        readToken();
        if (token.startsWith("php")) {
            token = token.substring(3).trim();
        }
    }


    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    public String nextElement() {
        return nextToken();
    }

    public boolean hasMoreTokens() {
        return token != null;
    }

    public String nextToken() {
        String tok = token;
        readToken();
        return tok;
    }

    private void readToken() {
        try {
            int c;
            char ch[];
            
            token = new String("");
            c = reader.read();
            while (c != -1 && (c != ';' || comment || inlineComment)) {
                if (comment) {
                    //token = new String("/*");
                    comment = false;
                    //c = reader.read();
                    while (c != -1) {
                        if (c == '*') {
                            token += "*";
                            c = reader.read();
                            if (c == '/') {
                                token += "/";
                                break;
                            } else if (c == -1) {
                                break;
                            }
                        } else {
                            ch = Character.toChars(c);
                            token += ch[0];
                            c = reader.read();
                        }
                    }
                    break;
                } else if (inlineComment) {
                    inlineComment = false;
                    //token = new String("//");
                    //System.out.println("Processing inline comment... token = " + token);
                    //System.out.println("line break = " + (int) '\n' + "; carriage return = " + (int) '\r');
                    while (c != -1) {
                        ch = Character.toChars(c);
                        token += ch[0];
                        //System.out.println(c);
                        if (c == '\n' || c == '\r') {
                            //System.out.println("inline comment - have a line break;");
                            break;
                        }
                        c = reader.read();
                    }
                    break;
                } else {

                    // string constant quoted by "
                    if (c == '"') {
                        ch = Character.toChars(c);
                        token += ch[0];
                        c = reader.read();
                        while (!(c == '"' || c == -1)) {
                            ch = Character.toChars(c);
                            token += ch[0];
                            if (c == '\\') {
                                c = reader.read();
                                if (c == '"') {
                                    ch = Character.toChars(c);
                                    token += ch[0];
                                    c = reader.read();
                                }
                            } else {
                                c = reader.read();
                            }
                        }
                    }

                    // string constant quoted by '
                    if (c == '\'') {
                        ch = Character.toChars(c);
                        token += ch[0];
                        c = reader.read();
                        while (!(c == '\'' || c == -1)) {
                            ch = Character.toChars(c);
                            token += ch[0];
                            if (c == '\\') {
                                c = reader.read();
                                if (c == '\'') {
                                    ch = Character.toChars(c);
                                    token += ch[0];
                                    c = reader.read();
                                }
                            } else {
                                c = reader.read();
                            }
                        }
                    }

                    // line breaks
                    /*
                    if (c == '\n' && (++breakLines > 1) && token.trim().isEmpty()) {
                        break;
                    }
                     */

                    // comments
                    if (c == '#') {
                        inlineComment = true;
                        token += "#";
                        c = reader.read();
                        //System.out.println("Inline comment starting with # found.");
                    } else if (c == '/') {
                        c = reader.read();
                        if (c == '/') {
                            inlineComment = true;
                            c = reader.read();
                            token += "//";
                            //break;
                        } else if (c == '*') {
                            comment = true;
                            c = reader.read();
                            token += "/*";
                            //break;
                        } else {
                            token += "/";
                        }

                    } else {
                        ch = Character.toChars(c);
                        token += ch[0];
                        c = reader.read();
                    }
                }
            }

            if (c == ';') {
                token += ";";
            }

            if (c == -1  && token.trim().isEmpty()) {
                //System.out.println("We are at the end");
                token = null;
            } else {
                //token = token.trim();
            }
        } catch (IOException ex) {
            token = null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal argument: token = " + token);
        }
    }

    private void findPhp() {
        try {
            int c = reader.read();
            while (c != -1) {
                if (c == '<') {
                    c = reader.read();
                    if (c == '?' || c == -1) {
                        break;
                    }
                } else {
                    c = reader.read();
                }
            }
        } catch (IOException ex) {
            token = null;
        }
    }
}
