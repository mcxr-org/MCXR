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
 * struct XrEventDataPassthroughStateChangedFB {
 *     XrStructureType type;
 *     void const * next;
 *     XrPassthroughStateChangedFlagsFB flags;
 * }</code></pre>
 */
public class XrEventDataPassthroughStateChangedFB extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        FLAGS;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(8)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        FLAGS = layout.offsetof(2);
    }

    /**
     * Creates a {@code XrEventDataPassthroughStateChangedFB} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrEventDataPassthroughStateChangedFB(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** @return the value of the {@code type} field. */
    @NativeType("XrStructureType")
    public int type() { return ntype(address()); }
    /** @return the value of the {@code next} field. */
    @NativeType("void const *")
    public long next() { return nnext(address()); }
    /** @return the value of the {@code flags} field. */
    @NativeType("XrPassthroughStateChangedFlagsFB")
    public long flags() { return nflags(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrEventDataPassthroughStateChangedFB type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link FBPassthrough#XR_TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB} value to the {@code type} field. */
    public XrEventDataPassthroughStateChangedFB type$Default() { return type(FBPassthrough.XR_TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB); }
    /** Sets the specified value to the {@code next} field. */
    public XrEventDataPassthroughStateChangedFB next(@NativeType("void const *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code flags} field. */
    public XrEventDataPassthroughStateChangedFB flags(@NativeType("XrPassthroughStateChangedFlagsFB") long value) { nflags(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrEventDataPassthroughStateChangedFB set(
        int type,
        long next,
        long flags
    ) {
        type(type);
        next(next);
        flags(flags);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrEventDataPassthroughStateChangedFB set(XrEventDataPassthroughStateChangedFB src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrEventDataPassthroughStateChangedFB} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrEventDataPassthroughStateChangedFB malloc() {
        return wrap(XrEventDataPassthroughStateChangedFB.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrEventDataPassthroughStateChangedFB} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrEventDataPassthroughStateChangedFB calloc() {
        return wrap(XrEventDataPassthroughStateChangedFB.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrEventDataPassthroughStateChangedFB} instance allocated with {@link BufferUtils}. */
    public static XrEventDataPassthroughStateChangedFB create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrEventDataPassthroughStateChangedFB.class, memAddress(container), container);
    }

    /** Returns a new {@code XrEventDataPassthroughStateChangedFB} instance for the specified memory address. */
    public static XrEventDataPassthroughStateChangedFB create(long address) {
        return wrap(XrEventDataPassthroughStateChangedFB.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrEventDataPassthroughStateChangedFB createSafe(long address) {
        return address == NULL ? null : wrap(XrEventDataPassthroughStateChangedFB.class, address);
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
    public static XrEventDataPassthroughStateChangedFB.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrEventDataPassthroughStateChangedFB} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrEventDataPassthroughStateChangedFB malloc(MemoryStack stack) {
        return wrap(XrEventDataPassthroughStateChangedFB.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrEventDataPassthroughStateChangedFB} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrEventDataPassthroughStateChangedFB calloc(MemoryStack stack) {
        return wrap(XrEventDataPassthroughStateChangedFB.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrEventDataPassthroughStateChangedFB.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrEventDataPassthroughStateChangedFB.NEXT); }
    /** Unsafe version of {@link #flags}. */
    public static long nflags(long struct) { return UNSAFE.getLong(null, struct + XrEventDataPassthroughStateChangedFB.FLAGS); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrEventDataPassthroughStateChangedFB.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrEventDataPassthroughStateChangedFB.NEXT, value); }
    /** Unsafe version of {@link #flags(long) flags}. */
    public static void nflags(long struct, long value) { UNSAFE.putLong(null, struct + XrEventDataPassthroughStateChangedFB.FLAGS, value); }

    // -----------------------------------

    /** An array of {@link XrEventDataPassthroughStateChangedFB} structs. */
    public static class Buffer extends StructBuffer<XrEventDataPassthroughStateChangedFB, Buffer> implements NativeResource {

        private static final XrEventDataPassthroughStateChangedFB ELEMENT_FACTORY = XrEventDataPassthroughStateChangedFB.create(-1L);

        /**
         * Creates a new {@code XrEventDataPassthroughStateChangedFB.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrEventDataPassthroughStateChangedFB#SIZEOF}, and its mark will be undefined.
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
        protected XrEventDataPassthroughStateChangedFB getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrEventDataPassthroughStateChangedFB.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void const *")
        public long next() { return XrEventDataPassthroughStateChangedFB.nnext(address()); }
        /** @return the value of the {@code flags} field. */
        @NativeType("XrPassthroughStateChangedFlagsFB")
        public long flags() { return XrEventDataPassthroughStateChangedFB.nflags(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrEventDataPassthroughStateChangedFB.ntype(address(), value); return this; }
        /** Sets the {@link FBPassthrough#XR_TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB} value to the {@code type} field. */
        public Buffer type$Default() { return type(FBPassthrough.XR_TYPE_EVENT_DATA_PASSTHROUGH_STATE_CHANGED_FB); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void const *") long value) { XrEventDataPassthroughStateChangedFB.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code flags} field. */
        public Buffer flags(@NativeType("XrPassthroughStateChangedFlagsFB") long value) { XrEventDataPassthroughStateChangedFB.nflags(address(), value); return this; }

    }

}