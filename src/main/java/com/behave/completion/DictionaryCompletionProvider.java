package com.behave.completion;

import com.behave.Helper;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;

import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

/**
 * @author jansorg
 */
class DictionaryCompletionProvider extends CompletionProvider<CompletionParameters> {
    private final boolean onlyManual;
    private static final Logger LOGGER = Logger.getInstance(DictionaryCompletionProvider.class);

    /**
     * @param onlyManual if true, then completions are only returned when the user manually requested it
     */
    DictionaryCompletionProvider(boolean onlyManual) {
        this.onlyManual = onlyManual;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        LOGGER.info("ENTERED EVENT");
//        if (parameters.isAutoPopup() && onlyManual) {
//            return;
//        }
        var document = parameters.getEditor().getDocument();
        var lines = document.getLineCount();
        var text = document.getText();
        var position = parameters.getOffset();
        var lineNumber = document.getLineNumber(position);
        var currentLine = text.split("\n")[lineNumber];
        LOGGER.info("CURRENT LINE: " + currentLine);
        if (isFeature(text, currentLine.trim().toLowerCase())) {

            String prefix = result.getPrefixMatcher().getPrefix();
            if (prefix.isEmpty()) {
                return;
            }

            // make sure that our prefix is the last word
            // for plain text file, all the content up to the caret is the prefix
            // we don't want that, because we're only completing a single word
            CompletionResultSet dictResult;
            int lastSpace = prefix.lastIndexOf(' ');
            if (lastSpace >= 0 && lastSpace < prefix.length() - 1) {
                prefix = prefix.substring(lastSpace + 1);
                dictResult = result.withPrefixMatcher(prefix);
            } else {
                dictResult = result;
            }

            int length = prefix.length();
            char firstChar = prefix.charAt(0);
            boolean isUppercase = Character.isUpperCase(firstChar);
            Dictionary.load();
            List<String> dicts = Dictionary.get();

            if (dicts == null || dicts.isEmpty()) {
                return;
            }

            for (var dict : dicts) {
                if (dict.toLowerCase( ).contains(currentLine.toLowerCase().trim())) {
                    result.addElement(LookupElementBuilder.create(getSuffix(currentLine, dict)));
                }
            }

        }

    }

    private String getSuffix(final String line, final String wholeLine) {
        return wholeLine.trim().substring(line.trim().length() - 1);
    }

     private boolean isFeature(final String text, final String currentLine) {
        var lines = cleanLines(text);
         return !lines.isEmpty() && lines.get(0).startsWith("feature:")
                 && currentLine.startsWith("when")
                 || currentLine.startsWith("given")
                 || currentLine.startsWith("then")
                 || currentLine.startsWith("and");
     }

     private List<String> cleanLines(final String text) {
        var lines = text.toLowerCase().trim().split("\n");
        List<String> result = new ArrayList<>();
        for (var line : lines) {
            var cleanLine = line.trim().split("#")[0];
            if (cleanLine != null && !cleanLine.isEmpty()) {
                result.add(cleanLine);
            }
        }
        return result;
     }
}