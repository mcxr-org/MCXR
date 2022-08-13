/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.*;

import java.nio.ByteBuffer;

import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrHolographicWindowAttachmentMSFT {
 *     XrStructureType type;
 *     void const * next;
 *     IUnknown * holographicSpace;
 *     IUnknown * coreWindow;
 * }</code></pre>
 */
public class XrHolographicWindowAttachmentMSFT extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        HOLOGRAPHICSPACE,
        COREWINDOW;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(POINTER_SIZE),
            __member(POINTER_SIZE)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        HOLOGRAPHICSPACE = layout.offsetof(2);
        COREWINDOW = layout.offsetof(3);
    }

    /**
     * Creates a {@code XrHolographicWindowAttachmentMSFT} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrHolographicWindowAttachmentMSFT(ByteBuffer container) {
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
    /**
     * @return a {@link PointerBuffer} view of the data pointed to by the {@code holographicSpace} field.
     *
     * @param capacity the number of elements in the returned buffer
     */
    @NativeType("IUnknown *")
    public PointerBuffer holographicSpace(int capacity) { return nholographicSpace(address(), capacity); }
    /**
     * @return a {@link PointerBuffer} view of the data pointed to by the {@code coreWindow} field.
     *
     * @param capacity the number of elements in the returned buffer
     */
    @NativeType("IUnknown *")
    public PointerBuffer coreWindow(int capacity) { return ncoreWindow(address(), capacity); }

    /** Sets the specified value to the {@code type} field. */
    public XrHolographicWindowAttachmentMSFT type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link MSFTHolographicWindowAttachment#XR_TYPE_HOLOGRAPHIC_WINDOW_ATTACHMENT_MSFT TYPE_HOLOGRAPHIC_WINDOW_ATTACHMENT_MSFT} value to the {@code type} field. */
    public XrHolographicWindowAttachmentMSFT type$Default() { return type(MSFTHolographicWindowAttachment.XR_TYPE_HOLOGRAPHIC_WINDOW_ATTACHMENT_MSFT); }
    /** Sets the specified value to the {@code next} field. */
    public XrHolographicWindowAttachmentMSFT next(@NativeType("void const *") long value) { nnext(address(), value); return this; }
    /** Sets the address of the specified {@link PointerBuffer} to the {@code holographicSpace} field. */
    public XrHolographicWindowAttachmentMSFT holographicSpace(@NativeType("IUnknown *") PointerBuffer value) { nholographicSpace(address(), value); return this; }
    /** Sets the address of the specified {@link PointerBuffer} to the {@code coreWindow} field. */
    public XrHolographicWindowAttachmentMSFT coreWindow(@NativeType("IUnknown *") PointerBuffer value) { ncoreWindow(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrHolographicWindowAttachmentMSFT set(
        int type,
        long next,
        PointerBuffer holographicSpace,
        PointerBuffer coreWindow
    ) {
        type(type);
        next(next);
        holographicSpace(holographicSpace);
        coreWindow(coreWindow);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrHolographicWindowAttachmentMSFT set(XrHolographicWindowAttachmentMSFT src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrHolographicWindowAttachmentMSFT} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrHolographicWindowAttachmentMSFT malloc() {
        return wrap(XrHolographicWindowAttachmentMSFT.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrHolographicWindowAttachmentMSFT} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrHolographicWindowAttachmentMSFT calloc() {
        return wrap(XrHolographicWindowAttachmentMSFT.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrHolographicWindowAttachmentMSFT} instance allocated with {@link BufferUtils}. */
    public static XrHolographicWindowAttachmentMSFT create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrHolographicWindowAttachmentMSFT.class, memAddress(container), container);
    }

    /** Returns a new {@code XrHolographicWindowAttachmentMSFT} instance for the specified memory address. */
    public static XrHolographicWindowAttachmentMSFT create(long address) {
        return wrap(XrHolographicWindowAttachmentMSFT.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrHolographicWindowAttachmentMSFT createSafe(long address) {
        return address == NULL ? null : wrap(XrHolographicWindowAttachmentMSFT.class, address);
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
    public static XrHolographicWindowAttachmentMSFT.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrHolographicWindowAttachmentMSFT} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrHolographicWindowAttachmentMSFT malloc(MemoryStack stack) {
        return wrap(XrHolographicWindowAttachmentMSFT.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrHolographicWindowAttachmentMSFT} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrHolographicWindowAttachmentMSFT calloc(MemoryStack stack) {
        return wrap(XrHolographicWindowAttachmentMSFT.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrHolographicWindowAttachmentMSFT.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrHolographicWindowAttachmentMSFT.NEXT); }
    /** Unsafe version of {@link #holographicSpace(int) holographicSpace}. */
    public static PointerBuffer nholographicSpace(long struct, int capacity) { return memPointerBuffer(memGetAddress(struct + XrHolographicWindowAttachmentMSFT.HOLOGRAPHICSPACE), capacity); }
    /** Unsafe version of {@link #coreWindow(int) coreWindow}. */
    public static PointerBuffer ncoreWindow(long struct, int capacity) { return memPointerBuffer(memGetAddress(struct + XrHolographicWindowAttachmentMSFT.COREWINDOW), capacity); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrHolographicWindowAttachmentMSFT.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrHolographicWindowAttachmentMSFT.NEXT, value); }
    /** Unsafe version of {@link #holographicSpace(PointerBuffer) holographicSpace}. */
    public static void nholographicSpace(long struct, PointerBuffer value) { memPutAddress(struct + XrHolographicWindowAttachmentMSFT.HOLOGRAPHICSPACE, memAddress(value)); }
    /** Unsafe version of {@link #coreWindow(PointerBuffer) coreWindow}. */
    public static void ncoreWindow(long struct, PointerBuffer value) { memPutAddress(struct + XrHolographicWindowAttachmentMSFT.COREWINDOW, memAddress(value)); }

    /**
     * Validates pointer members that should not be {@code NULL}.
     *
     * @param struct the struct to validate
     */
    public static void validate(long struct) {
        check(memGetAddress(struct + XrHolographicWindowAttachmentMSFT.HOLOGRAPHICSPACE));
        check(memGetAddress(struct + XrHolographicWindowAttachmentMSFT.COREWINDOW));
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

    /** An array of {@link XrHolographicWindowAttachmentMSFT} structs. */
    public static class Buffer extends StructBuffer<XrHolographicWindowAttachmentMSFT, Buffer> implements NativeResource {

        private static final XrHolographicWindowAttachmentMSFT ELEMENT_FACTORY = XrHolographicWindowAttachmentMSFT.create(-1L);

        /**
         * Creates a new {@code XrHolographicWindowAttachmentMSFT.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrHolographicWindowAttachmentMSFT#SIZEOF}, and its mark will be undefined.
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
        protected XrHolographicWindowAttachmentMSFT getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrHolographicWindowAttachmentMSFT.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void const *")
        public long next() { return XrHolographicWindowAttachmentMSFT.nnext(address()); }
        /**
         * @return a {@link PointerBuffer} view of the data pointed to by the {@code holographicSpace} field.
         *
         * @param capacity the number of elements in the returned buffer
         */
        @NativeType("IUnknown *")
        public PointerBuffer holographicSpace(int capacity) { return XrHolographicWindowAttachmentMSFT.nholographicSpace(address(), capacity); }
        /**
         * @return a {@link PointerBuffer} view of the data pointed to by the {@code coreWindow} field.
         *
         * @param capacity the number of elements in the returned buffer
         */
        @NativeType("IUnknown *")
        public PointerBuffer coreWindow(int capacity) { return XrHolographicWindowAttachmentMSFT.ncoreWindow(address(), capacity); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrHolographicWindowAttachmentMSFT.ntype(address(), value); return this; }
        /** Sets the {@link MSFTHolographicWindowAttachment#XR_TYPE_HOLOGRAPHIC_WINDOW_ATTACHMENT_MSFT TYPE_HOLOGRAPHIC_WINDOW_ATTACHMENT_MSFT} value to the {@code type} field. */
        public Buffer type$Default() { return type(MSFTHolographicWindowAttachment.XR_TYPE_HOLOGRAPHIC_WINDOW_ATTACHMENT_MSFT); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void const *") long value) { XrHolographicWindowAttachmentMSFT.nnext(address(), value); return this; }
        /** Sets the address of the specified {@link PointerBuffer} to the {@code holographicSpace} field. */
        public Buffer holographicSpace(@NativeType("IUnknown *") PointerBuffer value) { XrHolographicWindowAttachmentMSFT.nholographicSpace(address(), value); return this; }
        /** Sets the address of the specified {@link PointerBuffer} to the {@code coreWindow} field. */
        public Buffer coreWindow(@NativeType("IUnknown *") PointerBuffer value) { XrHolographicWindowAttachmentMSFT.ncoreWindow(address(), value); return this; }

    }

}