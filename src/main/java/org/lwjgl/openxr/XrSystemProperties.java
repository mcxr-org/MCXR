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
import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.MemoryStack.stackGet;
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

    /** Returns the value of the {@code type} field. */
    @NativeType("XrStructureType")
    public int type() { return ntype(address()); }
    /** Returns the value of the {@code next} field. */
    @NativeType("void *")
    public long next() { return nnext(address()); }
    /** Returns the value of the {@code systemId} field. */
    @NativeType("XrSystemId")
    public long systemId() { return nsystemId(address()); }
    /** Returns the value of the {@code vendorId} field. */
    @NativeType("uint32_t")
    public int vendorId() { return nvendorId(address()); }
    /** Returns a {@link ByteBuffer} view of the {@code systemName} field. */
    @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
    public ByteBuffer systemName() { return nsystemName(address()); }
    /** Decodes the null-terminated string stored in the {@code systemName} field. */
    @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
    public String systemNameString() { return nsystemNameString(address()); }
    /** Returns a {@link XrSystemGraphicsProperties} view of the {@code graphicsProperties} field. */
    public XrSystemGraphicsProperties graphicsProperties() { return ngraphicsProperties(address()); }
    /** Returns a {@link XrSystemTrackingProperties} view of the {@code trackingProperties} field. */
    public XrSystemTrackingProperties trackingProperties() { return ntrackingProperties(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrSystemProperties type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the specified value to the {@code next} field. */
    public XrSystemProperties next(@NativeType("void *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code systemId} field. */
    public XrSystemProperties systemId(@NativeType("XrSystemId") long value) { nsystemId(address(), value); return this; }
    /** Sets the specified value to the {@code vendorId} field. */
    public XrSystemProperties vendorId(@NativeType("uint32_t") int value) { nvendorId(address(), value); return this; }
    /** Copies the specified encoded string to the {@code systemName} field. */
    public XrSystemProperties systemName(@NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]") ByteBuffer value) { nsystemName(address(), value); return this; }
    /** Copies the specified {@link XrSystemGraphicsProperties} to the {@code graphicsProperties} field. */
    public XrSystemProperties graphicsProperties(XrSystemGraphicsProperties value) { ngraphicsProperties(address(), value); return this; }
    /** Passes the {@code graphicsProperties} field to the specified {@link java.util.function.Consumer Consumer}. */
    public XrSystemProperties graphicsProperties(java.util.function.Consumer<XrSystemGraphicsProperties> consumer) { consumer.accept(graphicsProperties()); return this; }
    /** Copies the specified {@link XrSystemTrackingProperties} to the {@code trackingProperties} field. */
    public XrSystemProperties trackingProperties(XrSystemTrackingProperties value) { ntrackingProperties(address(), value); return this; }
    /** Passes the {@code trackingProperties} field to the specified {@link java.util.function.Consumer Consumer}. */
    public XrSystemProperties trackingProperties(java.util.function.Consumer<XrSystemTrackingProperties> consumer) { consumer.accept(trackingProperties()); return this; }

    /** Initializes this struct with the specified values. */
    public XrSystemProperties set(
        int type,
        long next,
        long systemId,
        int vendorId,
        ByteBuffer systemName,
        XrSystemGraphicsProperties graphicsProperties,
        XrSystemTrackingProperties trackingProperties
    ) {
        type(type);
        next(next);
        systemId(systemId);
        vendorId(vendorId);
        systemName(systemName);
        graphicsProperties(graphicsProperties);
        trackingProperties(trackingProperties);

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

    // -----------------------------------

    /** Returns a new {@code XrSystemProperties} instance allocated on the thread-local {@link MemoryStack}. */
    public static XrSystemProperties mallocStack() {
        return mallocStack(stackGet());
    }

    /** Returns a new {@code XrSystemProperties} instance allocated on the thread-local {@link MemoryStack} and initializes all its bits to zero. */
    public static XrSystemProperties callocStack() {
        return callocStack(stackGet());
    }

    /**
     * Returns a new {@code XrSystemProperties} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSystemProperties mallocStack(MemoryStack stack) {
        return wrap(XrSystemProperties.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrSystemProperties} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSystemProperties callocStack(MemoryStack stack) {
        return wrap(XrSystemProperties.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the thread-local {@link MemoryStack}.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer mallocStack(int capacity) {
        return mallocStack(capacity, stackGet());
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the thread-local {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer callocStack(int capacity) {
        return callocStack(capacity, stackGet());
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer mallocStack(int capacity, MemoryStack stack) {
        return wrap(Buffer.class, stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer callocStack(int capacity, MemoryStack stack) {
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
    /** Unsafe version of {@link #systemId(long) systemId}. */
    public static void nsystemId(long struct, long value) { UNSAFE.putLong(null, struct + XrSystemProperties.SYSTEMID, value); }
    /** Unsafe version of {@link #vendorId(int) vendorId}. */
    public static void nvendorId(long struct, int value) { UNSAFE.putInt(null, struct + XrSystemProperties.VENDORID, value); }
    /** Unsafe version of {@link #systemName(ByteBuffer) systemName}. */
    public static void nsystemName(long struct, ByteBuffer value) {
        if (CHECKS) {
            checkNT1(value);
            checkGT(value, XR_MAX_SYSTEM_NAME_SIZE);
        }
        memCopy(memAddress(value), struct + XrSystemProperties.SYSTEMNAME, value.remaining());
    }
    /** Unsafe version of {@link #graphicsProperties(XrSystemGraphicsProperties) graphicsProperties}. */
    public static void ngraphicsProperties(long struct, XrSystemGraphicsProperties value) { memCopy(value.address(), struct + XrSystemProperties.GRAPHICSPROPERTIES, XrSystemGraphicsProperties.SIZEOF); }
    /** Unsafe version of {@link #trackingProperties(XrSystemTrackingProperties) trackingProperties}. */
    public static void ntrackingProperties(long struct, XrSystemTrackingProperties value) { memCopy(value.address(), struct + XrSystemProperties.TRACKINGPROPERTIES, XrSystemTrackingProperties.SIZEOF); }

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

        /** Returns the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrSystemProperties.ntype(address()); }
        /** Returns the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrSystemProperties.nnext(address()); }
        /** Returns the value of the {@code systemId} field. */
        @NativeType("XrSystemId")
        public long systemId() { return XrSystemProperties.nsystemId(address()); }
        /** Returns the value of the {@code vendorId} field. */
        @NativeType("uint32_t")
        public int vendorId() { return XrSystemProperties.nvendorId(address()); }
        /** Returns a {@link ByteBuffer} view of the {@code systemName} field. */
        @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
        public ByteBuffer systemName() { return XrSystemProperties.nsystemName(address()); }
        /** Decodes the null-terminated string stored in the {@code systemName} field. */
        @NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]")
        public String systemNameString() { return XrSystemProperties.nsystemNameString(address()); }
        /** Returns a {@link XrSystemGraphicsProperties} view of the {@code graphicsProperties} field. */
        public XrSystemGraphicsProperties graphicsProperties() { return XrSystemProperties.ngraphicsProperties(address()); }
        /** Returns a {@link XrSystemTrackingProperties} view of the {@code trackingProperties} field. */
        public XrSystemTrackingProperties trackingProperties() { return XrSystemProperties.ntrackingProperties(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrSystemProperties.ntype(address(), value); return this; }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrSystemProperties.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code systemId} field. */
        public Buffer systemId(@NativeType("XrSystemId") long value) { XrSystemProperties.nsystemId(address(), value); return this; }
        /** Sets the specified value to the {@code vendorId} field. */
        public Buffer vendorId(@NativeType("uint32_t") int value) { XrSystemProperties.nvendorId(address(), value); return this; }
        /** Copies the specified encoded string to the {@code systemName} field. */
        public Buffer systemName(@NativeType("char[XR_MAX_SYSTEM_NAME_SIZE]") ByteBuffer value) { XrSystemProperties.nsystemName(address(), value); return this; }
        /** Copies the specified {@link XrSystemGraphicsProperties} to the {@code graphicsProperties} field. */
        public Buffer graphicsProperties(XrSystemGraphicsProperties value) { XrSystemProperties.ngraphicsProperties(address(), value); return this; }
        /** Passes the {@code graphicsProperties} field to the specified {@link java.util.function.Consumer Consumer}. */
        public Buffer graphicsProperties(java.util.function.Consumer<XrSystemGraphicsProperties> consumer) { consumer.accept(graphicsProperties()); return this; }
        /** Copies the specified {@link XrSystemTrackingProperties} to the {@code trackingProperties} field. */
        public Buffer trackingProperties(XrSystemTrackingProperties value) { XrSystemProperties.ntrackingProperties(address(), value); return this; }
        /** Passes the {@code trackingProperties} field to the specified {@link java.util.function.Consumer Consumer}. */
        public Buffer trackingProperties(java.util.function.Consumer<XrSystemTrackingProperties> consumer) { consumer.accept(trackingProperties()); return this; }

    }

}