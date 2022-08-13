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

import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrScenePlaneMSFT {
 *     XrScenePlaneAlignmentTypeMSFT alignment;
 *     {@link XrExtent2Df XrExtent2Df} size;
 *     uint64_t meshBufferId;
 *     XrBool32 supportsIndicesUint16;
 * }</code></pre>
 */
public class XrScenePlaneMSFT extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        ALIGNMENT,
        SIZE,
        MESHBUFFERID,
        SUPPORTSINDICESUINT16;

    static {
        Layout layout = __struct(
            __member(4),
            __member(XrExtent2Df.SIZEOF, XrExtent2Df.ALIGNOF),
            __member(8),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        ALIGNMENT = layout.offsetof(0);
        SIZE = layout.offsetof(1);
        MESHBUFFERID = layout.offsetof(2);
        SUPPORTSINDICESUINT16 = layout.offsetof(3);
    }

    /**
     * Creates a {@code XrScenePlaneMSFT} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrScenePlaneMSFT(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** @return the value of the {@code alignment} field. */
    @NativeType("XrScenePlaneAlignmentTypeMSFT")
    public int alignment() { return nalignment(address()); }
    /** @return a {@link XrExtent2Df} view of the {@code size} field. */
    public XrExtent2Df size() { return nsize(address()); }
    /** @return the value of the {@code meshBufferId} field. */
    @NativeType("uint64_t")
    public long meshBufferId() { return nmeshBufferId(address()); }
    /** @return the value of the {@code supportsIndicesUint16} field. */
    @NativeType("XrBool32")
    public boolean supportsIndicesUint16() { return nsupportsIndicesUint16(address()) != 0; }

    /** Sets the specified value to the {@code alignment} field. */
    public XrScenePlaneMSFT alignment(@NativeType("XrScenePlaneAlignmentTypeMSFT") int value) { nalignment(address(), value); return this; }
    /** Copies the specified {@link XrExtent2Df} to the {@code size} field. */
    public XrScenePlaneMSFT size(XrExtent2Df value) { nsize(address(), value); return this; }
    /** Passes the {@code size} field to the specified {@link java.util.function.Consumer Consumer}. */
    public XrScenePlaneMSFT size(java.util.function.Consumer<XrExtent2Df> consumer) { consumer.accept(size()); return this; }
    /** Sets the specified value to the {@code meshBufferId} field. */
    public XrScenePlaneMSFT meshBufferId(@NativeType("uint64_t") long value) { nmeshBufferId(address(), value); return this; }
    /** Sets the specified value to the {@code supportsIndicesUint16} field. */
    public XrScenePlaneMSFT supportsIndicesUint16(@NativeType("XrBool32") boolean value) { nsupportsIndicesUint16(address(), value ? 1 : 0); return this; }

    /** Initializes this struct with the specified values. */
    public XrScenePlaneMSFT set(
        int alignment,
        XrExtent2Df size,
        long meshBufferId,
        boolean supportsIndicesUint16
    ) {
        alignment(alignment);
        size(size);
        meshBufferId(meshBufferId);
        supportsIndicesUint16(supportsIndicesUint16);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrScenePlaneMSFT set(XrScenePlaneMSFT src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrScenePlaneMSFT} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrScenePlaneMSFT malloc() {
        return wrap(XrScenePlaneMSFT.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrScenePlaneMSFT} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrScenePlaneMSFT calloc() {
        return wrap(XrScenePlaneMSFT.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrScenePlaneMSFT} instance allocated with {@link BufferUtils}. */
    public static XrScenePlaneMSFT create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrScenePlaneMSFT.class, memAddress(container), container);
    }

    /** Returns a new {@code XrScenePlaneMSFT} instance for the specified memory address. */
    public static XrScenePlaneMSFT create(long address) {
        return wrap(XrScenePlaneMSFT.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrScenePlaneMSFT createSafe(long address) {
        return address == NULL ? null : wrap(XrScenePlaneMSFT.class, address);
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
    public static XrScenePlaneMSFT.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrScenePlaneMSFT} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrScenePlaneMSFT malloc(MemoryStack stack) {
        return wrap(XrScenePlaneMSFT.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrScenePlaneMSFT} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrScenePlaneMSFT calloc(MemoryStack stack) {
        return wrap(XrScenePlaneMSFT.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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

    /** Unsafe version of {@link #alignment}. */
    public static int nalignment(long struct) { return UNSAFE.getInt(null, struct + XrScenePlaneMSFT.ALIGNMENT); }
    /** Unsafe version of {@link #size}. */
    public static XrExtent2Df nsize(long struct) { return XrExtent2Df.create(struct + XrScenePlaneMSFT.SIZE); }
    /** Unsafe version of {@link #meshBufferId}. */
    public static long nmeshBufferId(long struct) { return UNSAFE.getLong(null, struct + XrScenePlaneMSFT.MESHBUFFERID); }
    /** Unsafe version of {@link #supportsIndicesUint16}. */
    public static int nsupportsIndicesUint16(long struct) { return UNSAFE.getInt(null, struct + XrScenePlaneMSFT.SUPPORTSINDICESUINT16); }

    /** Unsafe version of {@link #alignment(int) alignment}. */
    public static void nalignment(long struct, int value) { UNSAFE.putInt(null, struct + XrScenePlaneMSFT.ALIGNMENT, value); }
    /** Unsafe version of {@link #size(XrExtent2Df) size}. */
    public static void nsize(long struct, XrExtent2Df value) { memCopy(value.address(), struct + XrScenePlaneMSFT.SIZE, XrExtent2Df.SIZEOF); }
    /** Unsafe version of {@link #meshBufferId(long) meshBufferId}. */
    public static void nmeshBufferId(long struct, long value) { UNSAFE.putLong(null, struct + XrScenePlaneMSFT.MESHBUFFERID, value); }
    /** Unsafe version of {@link #supportsIndicesUint16(boolean) supportsIndicesUint16}. */
    public static void nsupportsIndicesUint16(long struct, int value) { UNSAFE.putInt(null, struct + XrScenePlaneMSFT.SUPPORTSINDICESUINT16, value); }

    // -----------------------------------

    /** An array of {@link XrScenePlaneMSFT} structs. */
    public static class Buffer extends StructBuffer<XrScenePlaneMSFT, Buffer> implements NativeResource {

        private static final XrScenePlaneMSFT ELEMENT_FACTORY = XrScenePlaneMSFT.create(-1L);

        /**
         * Creates a new {@code XrScenePlaneMSFT.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrScenePlaneMSFT#SIZEOF}, and its mark will be undefined.
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
        protected XrScenePlaneMSFT getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code alignment} field. */
        @NativeType("XrScenePlaneAlignmentTypeMSFT")
        public int alignment() { return XrScenePlaneMSFT.nalignment(address()); }
        /** @return a {@link XrExtent2Df} view of the {@code size} field. */
        public XrExtent2Df size() { return XrScenePlaneMSFT.nsize(address()); }
        /** @return the value of the {@code meshBufferId} field. */
        @NativeType("uint64_t")
        public long meshBufferId() { return XrScenePlaneMSFT.nmeshBufferId(address()); }
        /** @return the value of the {@code supportsIndicesUint16} field. */
        @NativeType("XrBool32")
        public boolean supportsIndicesUint16() { return XrScenePlaneMSFT.nsupportsIndicesUint16(address()) != 0; }

        /** Sets the specified value to the {@code alignment} field. */
        public Buffer alignment(@NativeType("XrScenePlaneAlignmentTypeMSFT") int value) { XrScenePlaneMSFT.nalignment(address(), value); return this; }
        /** Copies the specified {@link XrExtent2Df} to the {@code size} field. */
        public Buffer size(XrExtent2Df value) { XrScenePlaneMSFT.nsize(address(), value); return this; }
        /** Passes the {@code size} field to the specified {@link java.util.function.Consumer Consumer}. */
        public Buffer size(java.util.function.Consumer<XrExtent2Df> consumer) { consumer.accept(size()); return this; }
        /** Sets the specified value to the {@code meshBufferId} field. */
        public Buffer meshBufferId(@NativeType("uint64_t") long value) { XrScenePlaneMSFT.nmeshBufferId(address(), value); return this; }
        /** Sets the specified value to the {@code supportsIndicesUint16} field. */
        public Buffer supportsIndicesUint16(@NativeType("XrBool32") boolean value) { XrScenePlaneMSFT.nsupportsIndicesUint16(address(), value ? 1 : 0); return this; }

    }

}