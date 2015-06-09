// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package com.tvl.util;

import java.util.Map;

public interface ImmutableMapBuilder<K, V> extends Map<K, V> {

    void add(K key, V value);

    void add(Map.Entry<? extends K, ? extends V> entry);

    V put(Map.Entry<? extends K, ? extends V> entry);

}
