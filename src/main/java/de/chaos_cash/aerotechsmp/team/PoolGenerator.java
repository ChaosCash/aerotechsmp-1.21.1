package de.chaos_cash.aerotechsmp.team;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PoolGenerator<T> {
    final List<PoolEntry<T>> entries = new ArrayList<>();
    int maxPos = 0;



    public PoolGenerator<T> withEntry(int weight, Supplier<T> output) {
        if (weight > 0) {
            maxPos += weight;
            entries.add(new PoolEntry<>(maxPos, output));
        }
        return this;
    }

    public PoolGenerator<T> withEntry(int weight, T output) {
        return withEntry(weight, () -> output);
    }


    public T getRandom() {
        if (entries.isEmpty()) return null;

        int randomPos = (int) (Math.random() * maxPos);
        for (var entry : entries)
            if (randomPos < entry.position)
                return entry.output.get();

        return entries.getLast().output.get();
    }


    public List<@NotNull T> getRandom(int pullCount) {
        var outputs = new ArrayList<T>();
        for (int i = 0; i < pullCount; i++) {
            var output = getRandom();
            if (output != null)
                outputs.add(output);
        }

        return outputs;
    }


    record PoolEntry<T>(int position, Supplier<T> output) {
    }
}
