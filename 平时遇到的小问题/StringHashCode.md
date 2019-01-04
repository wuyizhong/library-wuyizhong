```java
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
```
`String`的`hashCode`方法不断地乘以31
31可以被`JVM`优化，`31 * i = (i << 5) - i`。
