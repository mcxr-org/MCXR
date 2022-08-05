/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.*;

import java.nio.ByteBuffer;

import static org.lwjgl.openxr.XR10.XR_MAX_SYSTEM_NAME_SIZE;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrSystemProperties {
 *     XrStructureType type;
 *     void * next;
 *     XrSystemId systemId;
 *     uint32_t vendorId;
 *     char systemName[XR_MAX_SYSTEM_NAME_SIZE];
 *     {@link XrSystemGraphicsProperties XrSystemGraphicsProperties} graphicsProperties;
 *     {@link XrSystemTrackingProperties XrSystemTrackingProperties} trackingProperties;
 * }</code></pre>
 */
public class XrSystemProperties extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        SYSTEMID,
        VENDORID,
        SYSTEMNAME,
        GRAPHICSPROPERTIES,
        TRACKINGPROPERTIES;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(8),
            __member(4),
            __array(1, XR_MAX_SYSTEM_NAME_SIZE),
            __member(XrSystemGraphicsProperties.SIZEOF, XrSystemGraphicsProperties.ALIGNOF),
            __member(XrSystemTrackingProperties.SIZEOF, XrSystemTrackingProperties.ALIGNOF)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        SYSTEMID = layout.offsetof(2);
        VENDORID = layout.offsetof(3);
        SYSTEMNAME = layout.offsetof(4);
        GRAPHICSPROPERTIES = layout.offsetof(5);
        TRACKINGPROPERTIES = layout.offsetof(6);
    }

    /**
     * Creates a {@code XrSystemProperties} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrSystemProperties(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** @return the value of the {@code type} field. */
    @NativeType("XrStructureType")
    public int type() { return ntype(address()); }
    /** @return the value of the {@code next} field. */
    @NativeType("void *")
    public long next() { return nnext(address()); }
    /** @return the value of the {@code systemId} field. */
    @NativeType("XrSystemId")
    public long systemId() { return nsystemId(address()); }
    /** @return the value of the {@code vendorId} field. */
    @NativeType("uint32_t")
    public int vendorId() { return nvendorId(address()); }
    /** @return a {@link ByteBuffer} view of the {@code systemName} field. */
    @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
    public ByteBuffer systemName() { return nsystemName(address()); }
    /** @return the null-terminated string stored in the {@code systemName} field. */
    @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
    public String systemNameString() { return nsystemNameString(address()); }
    /** @return a {@link XrSystemGraphicsProperties} view of the {@code graphicsProperties} field. */
    public XrSystemGraphicsProperties graphicsProperties() { return ngraphicsProperties(address()); }
    /** @return a {@link XrSystemTrackingProperties} view of the {@code trackingProperties} field. */
    public XrSystemTrackingProperties trackingProperties() { return ntrackingProperties(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrSystemProperties type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link XR10#XR_TYPE_SYSTEM_PROPERTIES TYPE_SYSTEM_PROPERTIES} value to the {@code type} field. */
    public XrSystemProperties type$Default() { return type(XR10.XR_TYPE_SYSTEM_PROPERTIES); }
    /** Sets the specified value to the {@code next} field. */
    public XrSystemProperties next(@NativeType("void *") long value) { nnext(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrSystemProperties set(
        int type,
        long next
    ) {
        type(type);
        next(next);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrSystemProperties set(XrSystemProperties src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrSystemProperties} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrSystemProperties malloc() {
        return wrap(XrSystemProperties.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrSystemProperties} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrSystemProperties calloc() {
        return wrap(XrSystemProperties.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrSystemProperties} instance allocated with {@link BufferUtils}. */
    public static XrSystemProperties create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrSystemProperties.class, memAddress(container), container);
    }

    /** Returns a new {@code XrSystemProperties} instance for the specified memory address. */
    public static XrSystemProperties create(long address) {
        return wrap(XrSystemProperties.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrSystemProperties createSafe(long address) {
        return address == NULL ? null : wrap(XrSystemProperties.class, address);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer malloc(int capacity) {
        return wrap(Buffer.class, nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer calloc(int capacity) {
        return wrap(Buffer.class, nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return wrap(Buffer.class, memAddress(container), capacity, container);
    }

    /**
     * Create a {@link Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static Buffer create(long address, int capacity) {
        return wrap(Buffer.class, address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrSystemProperties.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrSystemProperties} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSystemProperties malloc(MemoryStack stack) {
        return wrap(XrSystemProperties.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrSystemProperties} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSystemProperties calloc(MemoryStack stack) {
        return wrap(XrSystemProperties.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer malloc(int capacity, MemoryStack stack) {
        return wrap(Buffer.class, stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer calloc(int capacity, MemoryStack stack) {
        return wrap(Buffer.class, stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #type}. */
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrSystemProperties.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrSystemProperties.NEXT); }
    /** Unsafe version of {@link #systemId}. */
    public static long nsystemId(long struct) { return UNSAFE.getLong(null, struct + XrSystemProperties.SYSTEMID); }
    /** Unsafe version of {@link #vendorId}. */
    public static int nvendorId(long struct) { return UNSAFE.getInt(null, struct + XrSystemProperties.VENDORID); }
    /** Unsafe version of {@link #systemName}. */
    public static ByteBuffer nsystemName(long struct) { return memByteBuffer(struct + XrSystemProperties.SYSTEMNAME, XR_MAX_SYSTEM_NAME_SIZE); }
    /** Unsafe version of {@link #systemNameString}. */
    public static String nsystemNameString(long struct) { return memUTF8(struct + XrSystemProperties.SYSTEMNAME); }
    /** Unsafe version of {@link #graphicsProperties}. */
    public static XrSystemGraphicsProperties ngraphicsProperties(long struct) { return XrSystemGraphicsProperties.create(struct + XrSystemProperties.GRAPHICSPROPERTIES); }
    /** Unsafe version of {@link #trackingProperties}. */
    public static XrSystemTrackingProperties ntrackingProperties(long struct) { return XrSystemTrackingProperties.create(struct + XrSystemProperties.TRACKINGPROPERTIES); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrSystemProperties.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrSystemProperties.NEXT, value); }

    // -----------------------------------

    /** An array of {@link XrSystemProperties} structs. */
    public static class Buffer extends StructBuffer<XrSystemProperties, Buffer> implements NativeResource {

        private static final XrSystemProperties ELEMENT_FACTORY = XrSystemProperties.create(-1L);

        /**
         * Creates a new {@code XrSystemProperties.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrSystemProperties#SIZEOF}, and its mark will be undefined.
         *
         * <p>The created buffer instance holds a strong reference to the container object.</p>
         */
        public Buffer(ByteBuffer container) {
            super(container, container.remaining() / SIZEOF);
        }

        public Buffer(long address, int cap) {
            super(address, null, -1, 0, cap, cap);
        }

        Buffer(long address, @Nullable ByteBuffer container, int mark, int pos, int lim, int cap) {
            super(address, container, mark, pos, lim, cap);
        }

        @Override
        protected Buffer self() {
            return this;
        }

        @Override
        protected XrSystemProperties getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrSystemProperties.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrSystemProperties.nnext(address()); }
        /** @return the value of the {@code systemId} field. */
        @NativeType("XrSystemId")
        public long systemId() { return XrSystemProperties.nsystemId(address()); }
        /** @return the value of the {@code vendorId} field. */
        @NativeType("uint32_t")
        public int vendorId() { return XrSystemProperties.nvendorId(address()); }
        /** @return a {@link ByteBuffer} view of the {@code systemName} field. */
        @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
        public ByteBuffer systemName() { return XrSystemProperties.nsystemName(address()); }
        /** @return the null-terminated string stored in the {@code systemName} field. */
        @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
        public String systemNameString() { return XrSystemProperties.nsystemNameString(address()); }
        /** @return a {@link XrSystemGraphicsProperties} view of the {@code graphicsProperties} field. */
        public XrSystemGraphicsProperties graphicsProperties() { return XrSystemProperties.ngraphicsProperties(address()); }
        /** @return a {@link XrSystemTrackingProperties} view of the {@code trackingProperties} field. */
        public XrSystemTrackingProperties trackingProperties() { return XrSystemProperties.ntrackingProperties(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrSystemProperties.ntype(address(), value); return this; }
        /** Sets the {@link XR10#XR_TYPE_SYSTEM_PROPERTIES TYPE_SYSTEM_PROPERTIES} value to the {@code type} field. */
        public Buffer type$Default() { return type(XR10.XR_TYPE_SYSTEM_PROPERTIES); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrSystemProperties.nnext(address(), value); return this; }

    }

}