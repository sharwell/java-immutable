// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package com.tvl.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;

public abstract class ImmutableDictionaryBuilderTestBase extends ImmutablesTestBase {
    @Test
    public void add() {
        ImmutableMapBuilder<String, Integer> builder = getBuilder();
        builder.add("five", 5);
        builder.add(new KeyValuePair<String, Integer>("six", 6));
        Assert.assertEquals(5, (int)builder.get("five"));
        Assert.assertEquals(6, (int)builder.get("six"));
        Assert.assertFalse(builder.containsKey("four"));
    }

    /**
     * Verifies that "adding" an entry to the dictionary that already exists with exactly the same key and value will
     * not throw an exception.
     */
    @Test
    public void addExactDuplicate() {
        ImmutableMapBuilder<String, Integer> builder = getBuilder();
        builder.add("five", 5);
        builder.add("five", 5);
        Assert.assertEquals(1, builder.size());
    }

    @Test
    public void addExistingKeyWithDifferentValue() {
        ImmutableMapBuilder<String, Integer> builder = getBuilder();
        builder.add("five", 5);
        thrown.expect(instanceOf(IllegalArgumentException.class));
        builder.add("five", 6);
    }

//[Fact]
//public void Indexer()
//{
//    var builder = this.GetBuilder<string, int>();

//    // Set and set again.
//    builder["five"] = 5;
//    Assert.Equal(5, builder["five"]);
//    builder["five"] = 5;
//    Assert.Equal(5, builder["five"]);

//    // Set to a new value.
//    builder["five"] = 50;
//    Assert.Equal(50, builder["five"]);

//    // Retrieve an invalid value.
//    Assert.Throws<KeyNotFoundException>(() => builder["foo"]);
//}

    @Test
    public void containsPair() {
        ImmutableMap<String, Integer> map = this.<String, Integer>getEmptyImmutableMap().add("five", 5);
        ImmutableMapBuilder<String, Integer> builder = getBuilder(map);
        Assert.assertTrue(builder.entrySet().contains(new KeyValuePair<String, Integer>("five", 5)));
    }

    @Test
    public void removePair() {
        ImmutableMap<String, Integer> map = this.<String, Integer>getEmptyImmutableMap().add("five", 5).add("six", 6);
        ImmutableMapBuilder<String, Integer> builder = getBuilder(map);
        Assert.assertTrue(builder.entrySet().remove(new KeyValuePair<String, Integer>("five", 5)));
        Assert.assertFalse(builder.entrySet().remove(new KeyValuePair<String, Integer>("foo", 1)));
        Assert.assertEquals(1, builder.size());
        Assert.assertEquals(6, (int)builder.get("six"));
    }

    @Test
    public void removeKey() {
        ImmutableMap<String, Integer> map = this.<String, Integer>getEmptyImmutableMap().add("five", 5).add("six", 6);
        ImmutableMapBuilder<String, Integer> builder = getBuilder(map);
        builder.remove("five");
        Assert.assertEquals(1, builder.size());
        Assert.assertEquals(6, (int)builder.get("six"));
    }

//[Fact]
//public void CopyTo()
//{
//    var map = this.GetEmptyImmutableDictionary<string, int>().Add("five", 5);
//    var builder = this.GetBuilder(map);
//    var array = new KeyValuePair<string, int>[2]; // intentionally larger than source.
//    builder.CopyTo(array, 1);
//    Assert.Equal(new KeyValuePair<string, int>(), array[0]);
//    Assert.Equal(new KeyValuePair<string, int>("five", 5), array[1]);

//    Assert.Throws<ArgumentNullException>(() => builder.CopyTo(null, 0));
//}

//[Fact]
//public void IsReadOnly()
//{
//    var builder = this.GetBuilder<string, int>();
//    Assert.False(builder.IsReadOnly);
//}

    @Test
    public void keySet() {
        ImmutableMap<String, Integer> map = this.<String, Integer>getEmptyImmutableMap().add("five", 5).add("six", 6);
        ImmutableMapBuilder<String, Integer> builder = getBuilder(map);
        assertEquivalentSequences(Arrays.asList("five", "six"), builder.keySet());
    }

    @Test
    public void values() {
        ImmutableMap<String, Integer> map = this.<String, Integer>getEmptyImmutableMap().add("five", 5).add("six", 6);
        ImmutableMapBuilder<String, Integer> builder = getBuilder(map);
        assertEquivalentSequences(Arrays.asList(5, 6), builder.values());
    }

    @Test
    public void getValue() {
        ImmutableMap<String, Integer> map = this.<String, Integer>getEmptyImmutableMap().add("five", 5).add("six", 6);
        ImmutableMapBuilder<String, Integer> builder = getBuilder(map);

        Integer value = builder.get("five");
        Assert.assertEquals(5, (int)value);

        value = builder.get("six");
        Assert.assertEquals(6, (int)value);

        value = builder.get("four");
        Assert.assertNull(value);
    }

    @Test
    public void getKey() {
        ImmutableMapBuilder<String, Integer> builder = TestExtensionMethods.toBuilder(this.<Integer>empty(ordinalIgnoreCaseComparator())
            .add("a", 1));
        String actualKey = getKeyHelper(builder, "a");
        Assert.assertEquals("a", actualKey);

        actualKey = getKeyHelper(builder, "A");
        Assert.assertEquals("a", actualKey);

        actualKey = getKeyHelper(builder, "b");
        Assert.assertNull(actualKey);
    }

    @Test
    public void iterateTest() {
        ImmutableMap<String, Integer> map = this.<String, Integer>getEmptyImmutableMap().add("five", 5).add("six", 6);
        ImmutableMapBuilder<String, Integer> builder = getBuilder(map);
        Iterator<Map.Entry<String, Integer>> iterator = builder.entrySet().iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());

        Iterator<Map.Entry<String, Integer>> manualIter = builder.entrySet().iterator();
        while (manualIter.hasNext()) {
            manualIter.next();
        }
        Assert.assertFalse(manualIter.hasNext());
        thrown.expect(instanceOf(NoSuchElementException.class));
        manualIter.next();
    }

//[Fact]
//public void IDictionaryMembers()
//{
//    var builder = this.GetBuilder<string, int>();
//    var dictionary = (IDictionary)builder;
//    dictionary.Add("a", 1);
//    Assert.True(dictionary.Contains("a"));
//    Assert.Equal(1, dictionary["a"]);
//    Assert.Equal(new[] { "a" }, dictionary.Keys.Cast<string>().ToArray());
//    Assert.Equal(new[] { 1 }, dictionary.Values.Cast<int>().ToArray());
//    dictionary["a"] = 2;
//    Assert.Equal(2, dictionary["a"]);
//    dictionary.Remove("a");
//    Assert.False(dictionary.Contains("a"));
//    Assert.False(dictionary.IsFixedSize);
//    Assert.False(dictionary.IsReadOnly);
//}

//[Fact]
//public void IDictionaryEnumerator()
//{
//    var builder = this.GetBuilder<string, int>();
//    var dictionary = (IDictionary)builder;
//    dictionary.Add("a", 1);
//    var enumerator = dictionary.GetEnumerator();
//    Assert.Throws<InvalidOperationException>(() => enumerator.Current);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Key);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Value);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Entry);
//    Assert.True(enumerator.MoveNext());
//    Assert.Equal(enumerator.Entry, enumerator.Current);
//    Assert.Equal(enumerator.Key, enumerator.Entry.Key);
//    Assert.Equal(enumerator.Value, enumerator.Entry.Value);
//    Assert.Equal("a", enumerator.Key);
//    Assert.Equal(1, enumerator.Value);
//    Assert.False(enumerator.MoveNext());
//    Assert.Throws<InvalidOperationException>(() => enumerator.Current);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Key);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Value);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Entry);
//    Assert.False(enumerator.MoveNext());

//    enumerator.Reset();
//    Assert.Throws<InvalidOperationException>(() => enumerator.Current);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Key);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Value);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Entry);
//    Assert.True(enumerator.MoveNext());
//    Assert.Equal(enumerator.Key, ((DictionaryEntry)enumerator.Current).Key);
//    Assert.Equal(enumerator.Value, ((DictionaryEntry)enumerator.Current).Value);
//    Assert.Equal("a", enumerator.Key);
//    Assert.Equal(1, enumerator.Value);
//    Assert.False(enumerator.MoveNext());
//    Assert.Throws<InvalidOperationException>(() => enumerator.Current);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Key);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Value);
//    Assert.Throws<InvalidOperationException>(() => enumerator.Entry);
//    Assert.False(enumerator.MoveNext());
//}

//[Fact]
//public void ICollectionMembers()
//{
//    var builder = this.GetBuilder<string, int>();
//    var collection = (ICollection)builder;

//    collection.CopyTo(new object[0], 0);

//    builder.Add("b", 2);
//    Assert.True(builder.ContainsKey("b"));

//    var array = new object[builder.Count + 1];
//    collection.CopyTo(array, 1);
//    Assert.Null(array[0]);
//    Assert.Equal(new object[] { null, new DictionaryEntry("b", 2), }, array);

//    Assert.False(collection.IsSynchronized);
//    Assert.NotNull(collection.SyncRoot);
//    Assert.Same(collection.SyncRoot, collection.SyncRoot);
//}

    protected abstract <K, V> K getKeyHelper(Map<K, V> map, K equalKey);

    /**
     * Gets the {@code Builder} for a given map instance.
     *
     * @param <K> The type of key.
     * @param <V> The type of value.
     * @param basis The map.
     * @return The builder.
     */
    protected abstract <K, V> ImmutableMapBuilder<K, V> getBuilder(ImmutableMap<K, V> basis);

    protected final <K, V> ImmutableMapBuilder<K, V> getBuilder() {
        return getBuilder(null);
    }

    /**
     * Gets an empty immutable map.
     *
     * @param <K> The type of key.
     * @param <V> The type of value.
     * @return An empty immutable map.
     */
    protected abstract <K, V> ImmutableMap<K, V> getEmptyImmutableMap();

    protected abstract <V> ImmutableMap<String, V> empty(StringComparator comparator);
}
