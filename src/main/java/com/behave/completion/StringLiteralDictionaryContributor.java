package com.behave.completion;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PlainTextTokenTypes;

/**
 * @author jansorg
 */
public class StringLiteralDictionaryContributor extends CompletionContributor {
    private static final Logger LOGGER = Logger.getInstance(StringLiteralDictionaryContributor.class);

    public StringLiteralDictionaryContributor() {
        LOGGER.info("BEHAVE PLUGIN IS WORKIN");
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(PlainTextTokenTypes.PLAIN_TEXT),
                new DictionaryCompletionProvider(false));


        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().with(new StringLiteralPattern()),
                new DictionaryCompletionProvider(false));


        extend(CompletionType.BASIC,
                PlatformPatterns.not(PlatformPatterns.alwaysFalse()),
                new DictionaryCompletionProvider(true));
    }
}