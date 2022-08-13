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

import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrPassthroughLayerCreateInfoFB {
 *     XrStructureType type;
 *     void const * next;
 *     XrPassthroughFB passthrough;
 *     XrPassthroughFlagsFB flags;
 *     XrPassthroughLayerPurposeFB purpose;
 * }</code></pre>
 */
public class XrPassthroughLayerCreateInfoFB extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        PASSTHROUGH,
        FLAGS,
        PURPOSE;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(POINTER_SIZE),
            __member(8),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        PASSTHROUGH = layout.offsetof(2);
        FLAGS = layout.offsetof(3);
        PURPOSE = layout.offsetof(4);
    }

    /**
     * Creates a {@code XrPassthroughLayerCreateInfoFB} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrPassthroughLayerCreateInfoFB(ByteBuffer container) {
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
    /** @return the value of the {@code passthrough} field. */
    @NativeType("XrPassthroughFB")
    public long passthrough() { return npassthrough(address()); }
    /** @return the value of the {@code flags} field. */
    @NativeType("XrPassthroughFlagsFB")
    public long flags() { return nflags(address()); }
    /** @return the value of the {@code purpose} field. */
    @NativeType("XrPassthroughLayerPurposeFB")
    public int purpose() { return npurpose(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrPassthroughLayerCreateInfoFB type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link FBPassthrough#XR_TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB} value to the {@code type} field. */
    public XrPassthroughLayerCreateInfoFB type$Default() { return type(FBPassthrough.XR_TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB); }
    /** Sets the specified value to the {@code next} field. */
    public XrPassthroughLayerCreateInfoFB next(@NativeType("void const *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code passthrough} field. */
    public XrPassthroughLayerCreateInfoFB passthrough(XrPassthroughFB value) { npassthrough(address(), value); return this; }
    /** Sets the specified value to the {@code flags} field. */
    public XrPassthroughLayerCreateInfoFB flags(@NativeType("XrPassthroughFlagsFB") long value) { nflags(address(), value); return this; }
    /** Sets the specified value to the {@code purpose} field. */
    public XrPassthroughLayerCreateInfoFB purpose(@NativeType("XrPassthroughLayerPurposeFB") int value) { npurpose(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrPassthroughLayerCreateInfoFB set(
        int type,
        long next,
        XrPassthroughFB passthrough,
        long flags,
        int purpose
    ) {
        type(type);
        next(next);
        passthrough(passthrough);
        flags(flags);
        purpose(purpose);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrPassthroughLayerCreateInfoFB set(XrPassthroughLayerCreateInfoFB src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrPassthroughLayerCreateInfoFB} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrPassthroughLayerCreateInfoFB malloc() {
        return wrap(XrPassthroughLayerCreateInfoFB.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrPassthroughLayerCreateInfoFB} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrPassthroughLayerCreateInfoFB calloc() {
        return wrap(XrPassthroughLayerCreateInfoFB.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrPassthroughLayerCreateInfoFB} instance allocated with {@link BufferUtils}. */
    public static XrPassthroughLayerCreateInfoFB create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrPassthroughLayerCreateInfoFB.class, memAddress(container), container);
    }

    /** Returns a new {@code XrPassthroughLayerCreateInfoFB} instance for the specified memory address. */
    public static XrPassthroughLayerCreateInfoFB create(long address) {
        return wrap(XrPassthroughLayerCreateInfoFB.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrPassthroughLayerCreateInfoFB createSafe(long address) {
        return address == NULL ? null : wrap(XrPassthroughLayerCreateInfoFB.class, address);
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
    public static XrPassthroughLayerCreateInfoFB.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrPassthroughLayerCreateInfoFB} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrPassthroughLayerCreateInfoFB malloc(MemoryStack stack) {
        return wrap(XrPassthroughLayerCreateInfoFB.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrPassthroughLayerCreateInfoFB} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrPassthroughLayerCreateInfoFB calloc(MemoryStack stack) {
        return wrap(XrPassthroughLayerCreateInfoFB.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrPassthroughLayerCreateInfoFB.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrPassthroughLayerCreateInfoFB.NEXT); }
    /** Unsafe version of {@link #passthrough}. */
    public static long npassthrough(long struct) { return memGetAddress(struct + XrPassthroughLayerCreateInfoFB.PASSTHROUGH); }
    /** Unsafe version of {@link #flags}. */
    public static long nflags(long struct) { return UNSAFE.getLong(null, struct + XrPassthroughLayerCreateInfoFB.FLAGS); }
    /** Unsafe version of {@link #purpose}. */
    public static int npurpose(long struct) { return UNSAFE.getInt(null, struct + XrPassthroughLayerCreateInfoFB.PURPOSE); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrPassthroughLayerCreateInfoFB.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrPassthroughLayerCreateInfoFB.NEXT, value); }
    /** Unsafe version of {@link #passthrough(XrPassthroughFB) passthrough}. */
    public static void npassthrough(long struct, XrPassthroughFB value) { memPutAddress(struct + XrPassthroughLayerCreateInfoFB.PASSTHROUGH, value.address()); }
    /** Unsafe version of {@link #flags(long) flags}. */
    public static void nflags(long struct, long value) { UNSAFE.putLong(null, struct + XrPassthroughLayerCreateInfoFB.FLAGS, value); }
    /** Unsafe version of {@link #purpose(int) purpose}. */
    public static void npurpose(long struct, int value) { UNSAFE.putInt(null, struct + XrPassthroughLayerCreateInfoFB.PURPOSE, value); }

    /**
     * Validates pointer members that should not be {@code NULL}.
     *
     * @param struct the struct to validate
     */
    public static void validate(long struct) {
        check(memGetAddress(struct + XrPassthroughLayerCreateInfoFB.PASSTHROUGH));
    }

    /**
     * Calls {@link #validate(long)} for each struct contained in the specified struct array.
     *
     * @param array the struct array to validate
     * @param count the number of structs in {@code array}
     */
    public static void validate(long array, int count) {
        for (int i = 0; i < count; i++) {
            validate(array + Integer.toUnsignedLong(i) * SIZEOF);
        }
    }

    // -----------------------------------

    /** An array of {@link XrPassthroughLayerCreateInfoFB} structs. */
    public static class Buffer extends StructBuffer<XrPassthroughLayerCreateInfoFB, Buffer> implements NativeResource {

        private static final XrPassthroughLayerCreateInfoFB ELEMENT_FACTORY = XrPassthroughLayerCreateInfoFB.create(-1L);

        /**
         * Creates a new {@code XrPassthroughLayerCreateInfoFB.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrPassthroughLayerCreateInfoFB#SIZEOF}, and its mark will be undefined.
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
        protected XrPassthroughLayerCreateInfoFB getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrPassthroughLayerCreateInfoFB.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void const *")
        public long next() { return XrPassthroughLayerCreateInfoFB.nnext(address()); }
        /** @return the value of the {@code passthrough} field. */
        @NativeType("XrPassthroughFB")
        public long passthrough() { return XrPassthroughLayerCreateInfoFB.npassthrough(address()); }
        /** @return the value of the {@code flags} field. */
        @NativeType("XrPassthroughFlagsFB")
        public long flags() { return XrPassthroughLayerCreateInfoFB.nflags(address()); }
        /** @return the value of the {@code purpose} field. */
        @NativeType("XrPassthroughLayerPurposeFB")
        public int purpose() { return XrPassthroughLayerCreateInfoFB.npurpose(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrPassthroughLayerCreateInfoFB.ntype(address(), value); return this; }
        /** Sets the {@link FBPassthrough#XR_TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB} value to the {@code type} field. */
        public Buffer type$Default() { return type(FBPassthrough.XR_TYPE_PASSTHROUGH_LAYER_CREATE_INFO_FB); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void const *") long value) { XrPassthroughLayerCreateInfoFB.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code passthrough} field. */
        public Buffer passthrough(XrPassthroughFB value) { XrPassthroughLayerCreateInfoFB.npassthrough(address(), value); return this; }
        /** Sets the specified value to the {@code flags} field. */
        public Buffer flags(@NativeType("XrPassthroughFlagsFB") long value) { XrPassthroughLayerCreateInfoFB.nflags(address(), value); return this; }
        /** Sets the specified value to the {@code purpose} field. */
        public Buffer purpose(@NativeType("XrPassthroughLayerPurposeFB") int value) { XrPassthroughLayerCreateInfoFB.npurpose(address(), value); return this; }

    }

}