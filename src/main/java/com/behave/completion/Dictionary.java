package com.behave.completion;

import com.intellij.spellchecker.BundledDictionaryProvider;
import com.intellij.spellchecker.SpellCheckerManager;

import com.intellij.spellchecker.engine.Transformation;
import com.intellij.util.containers.ContainerUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jansorg
 */
class Dictionary {
    private static List<String> bundledDictionaries = new ArrayList<>();

    // load() loads all build-in dictionaries in a thread-safe manner
    public static void load() {

        bundledDictionaries = DictionaryHelper.getDictionary();
    }

    public static List<String> get() {
        return bundledDictionaries;
    }
}