package carbonbutterfly.generateqti.utils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Useful static functions for processing text
 */
public class Utils {

    public static final String PRE_AMBLE = """
           Quiz title: CS-110 MCQ Class Test
           Quiz description: <ul><li>Answer all the questions.</li>\
           <li>IF YOU COMMUNICATE WITH ANYONE OR ANYTHING IN ANYWAY YOU ARE COMMITTING ACADEMIC MISCONDUCT.</li>\
           <li>This includes talking and using any device other than the computer you use for the test.</li>\
           <li>The penalties for misconduct in exams/tests are MUCH more serious than for coursework and it is \
           perfectly possible to be WITHDRAWN FROM THE UNIVERSITY FOR A FIRST OFFENCE.</li>\
           <li>You have 2 hours to complete this test and the programming test - you can choose how much time you spend
           on each part.</li>\
           <li>By submitting you state that you fully understand and are complying with the University's \
           Academic Misconduct Policy.</li>\
           </ul>
            """;

    /**
     * Unescape the two escape sequences used (for < and &) - not 100% clear this is necessary
     *
     * @param text string to be 'unescaped'
     * @return 'unescaped' text
     */
    public static String unEscape(final String text) {
        return text.replaceAll("&lt;", "<").replaceAll("&amp;", "&");
    }

    /**
     * Deal with formatting issues with incoming text and return them as a list, one line per item
     * The issues generally are:
     * 1. Leading newlines/line feeds and trailing white space on the original text
     * 2. Leading newlines/line feeds and trailing white space on each line once broken up by line
     * 3. Indenting based on the structure of the XML if formatted/indented for readability
     * All this is stripped out *except* the indenting specifically in code blocks
     * (consistent indenting is added back later to ensure well-formed input for text2qti
     *
     * @param text "Untidy" text
     * @return tidied-up text as a list, one line per item
     */
    public static List<String> cleanUpTextToList(final String text) {
        final String unEscaped = unEscape(text)
                .replaceAll("^[\n\r]+", "")
                .replaceAll("[\n\r\t ]+$", "");
        final String[] splitByLine = unEscaped.split("\n");
        //Get rid of leading newlines
        List<String> tidiedUp = Arrays.stream(splitByLine)
                .map(line -> line.replaceAll("^[\n\r]+", "").replaceAll("[\n\r\t ]+$", ""))
                .toList();

        final int countSpaces = tidiedUp.get(0).indexOf(tidiedUp.get(0).trim()); //Count spaces at start of first line

        return tidiedUp.stream()
                .map(line -> line.substring(Math.min(countSpaces, line.indexOf(line.trim()))))
                .toList();
    }

    /**
     * Generate an appropriately-indented block of text, with an initial unindented label
     * All lines of content must be consistently indented and the label (question number or answer number) must fit
     * within that indenting (i.e. the label is *not* indented). This means that the indenting must be strictly >
     * than the length of the label (because text2qti doesn't work if there is space after the question number). In practice
     * this means an indent of 5 at least, assuming a max of 999 questions (there are 100 in the bank at the time of writing
     * this and it's helpful to be able to generate a "quiz" with all questions for checking).
     *
     * @param indent The amount of indenting - a +ve integer (usually 5)
     * @param label  The label that appears at the start of the first line - either the question or answer number
     * @param text   List of lines of text
     * @return
     */
    public static String outputTextBlock(final int indent, final String label, final List<String> text) {
        final StringBuilder builder = new StringBuilder();
        builder.append(label).append(" ".repeat(indent - label.length())).append(text.get(0)).append("\n");
        text.stream().skip(1).forEach(item -> builder.append(" ".repeat(indent)).append(item).append("\n"));
        return builder.toString();
    }
}
