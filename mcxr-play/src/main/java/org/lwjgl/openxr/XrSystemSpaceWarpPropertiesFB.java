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
 * struct XrSystemSpaceWarpPropertiesFB {
 *     XrStructureType type;
 *     void * next;
 *     uint32_t recommendedMotionVectorImageRectWidth;
 *     uint32_t recommendedMotionVectorImageRectHeight;
 * }</code></pre>
 */
public class XrSystemSpaceWarpPropertiesFB extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        RECOMMENDEDMOTIONVECTORIMAGERECTWIDTH,
        RECOMMENDEDMOTIONVECTORIMAGERECTHEIGHT;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        RECOMMENDEDMOTIONVECTORIMAGERECTWIDTH = layout.offsetof(2);
        RECOMMENDEDMOTIONVECTORIMAGERECTHEIGHT = layout.offsetof(3);
    }

    /**
     * Creates a {@code XrSystemSpaceWarpPropertiesFB} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrSystemSpaceWarpPropertiesFB(ByteBuffer container) {
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
    /** @return the value of the {@code recommendedMotionVectorImageRectWidth} field. */
    @NativeType("uint32_t")
    public int recommendedMotionVectorImageRectWidth() { return nrecommendedMotionVectorImageRectWidth(address()); }
    /** @return the value of the {@code recommendedMotionVectorImageRectHeight} field. */
    @NativeType("uint32_t")
    public int recommendedMotionVectorImageRectHeight() { return nrecommendedMotionVectorImageRectHeight(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrSystemSpaceWarpPropertiesFB type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link FBSpaceWarp#XR_TYPE_SYSTEM_SPACE_WARP_PROPERTIES_FB TYPE_SYSTEM_SPACE_WARP_PROPERTIES_FB} value to the {@code type} field. */
    public XrSystemSpaceWarpPropertiesFB type$Default() { return type(FBSpaceWarp.XR_TYPE_SYSTEM_SPACE_WARP_PROPERTIES_FB); }
    /** Sets the specified value to the {@code next} field. */
    public XrSystemSpaceWarpPropertiesFB next(@NativeType("void *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code recommendedMotionVectorImageRectWidth} field. */
    public XrSystemSpaceWarpPropertiesFB recommendedMotionVectorImageRectWidth(@NativeType("uint32_t") int value) { nrecommendedMotionVectorImageRectWidth(address(), value); return this; }
    /** Sets the specified value to the {@code recommendedMotionVectorImageRectHeight} field. */
    public XrSystemSpaceWarpPropertiesFB recommendedMotionVectorImageRectHeight(@NativeType("uint32_t") int value) { nrecommendedMotionVectorImageRectHeight(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrSystemSpaceWarpPropertiesFB set(
        int type,
        long next,
        int recommendedMotionVectorImageRectWidth,
        int recommendedMotionVectorImageRectHeight
    ) {
        type(type);
        next(next);
        recommendedMotionVectorImageRectWidth(recommendedMotionVectorImageRectWidth);
        recommendedMotionVectorImageRectHeight(recommendedMotionVectorImageRectHeight);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrSystemSpaceWarpPropertiesFB set(XrSystemSpaceWarpPropertiesFB src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrSystemSpaceWarpPropertiesFB} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrSystemSpaceWarpPropertiesFB malloc() {
        return wrap(XrSystemSpaceWarpPropertiesFB.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrSystemSpaceWarpPropertiesFB} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrSystemSpaceWarpPropertiesFB calloc() {
        return wrap(XrSystemSpaceWarpPropertiesFB.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrSystemSpaceWarpPropertiesFB} instance allocated with {@link BufferUtils}. */
    public static XrSystemSpaceWarpPropertiesFB create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrSystemSpaceWarpPropertiesFB.class, memAddress(container), container);
    }

    /** Returns a new {@code XrSystemSpaceWarpPropertiesFB} instance for the specified memory address. */
    public static XrSystemSpaceWarpPropertiesFB create(long address) {
        return wrap(XrSystemSpaceWarpPropertiesFB.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrSystemSpaceWarpPropertiesFB createSafe(long address) {
        return address == NULL ? null : wrap(XrSystemSpaceWarpPropertiesFB.class, address);
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
    public static XrSystemSpaceWarpPropertiesFB.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrSystemSpaceWarpPropertiesFB} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSystemSpaceWarpPropertiesFB malloc(MemoryStack stack) {
        return wrap(XrSystemSpaceWarpPropertiesFB.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrSystemSpaceWarpPropertiesFB} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSystemSpaceWarpPropertiesFB calloc(MemoryStack stack) {
        return wrap(XrSystemSpaceWarpPropertiesFB.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrSystemSpaceWarpPropertiesFB.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrSystemSpaceWarpPropertiesFB.NEXT); }
    /** Unsafe version of {@link #recommendedMotionVectorImageRectWidth}. */
    public static int nrecommendedMotionVectorImageRectWidth(long struct) { return UNSAFE.getInt(null, struct + XrSystemSpaceWarpPropertiesFB.RECOMMENDEDMOTIONVECTORIMAGERECTWIDTH); }
    /** Unsafe version of {@link #recommendedMotionVectorImageRectHeight}. */
    public static int nrecommendedMotionVectorImageRectHeight(long struct) { return UNSAFE.getInt(null, struct + XrSystemSpaceWarpPropertiesFB.RECOMMENDEDMOTIONVECTORIMAGERECTHEIGHT); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrSystemSpaceWarpPropertiesFB.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrSystemSpaceWarpPropertiesFB.NEXT, value); }
    /** Unsafe version of {@link #recommendedMotionVectorImageRectWidth(int) recommendedMotionVectorImageRectWidth}. */
    public static void nrecommendedMotionVectorImageRectWidth(long struct, int value) { UNSAFE.putInt(null, struct + XrSystemSpaceWarpPropertiesFB.RECOMMENDEDMOTIONVECTORIMAGERECTWIDTH, value); }
    /** Unsafe version of {@link #recommendedMotionVectorImageRectHeight(int) recommendedMotionVectorImageRectHeight}. */
    public static void nrecommendedMotionVectorImageRectHeight(long struct, int value) { UNSAFE.putInt(null, struct + XrSystemSpaceWarpPropertiesFB.RECOMMENDEDMOTIONVECTORIMAGERECTHEIGHT, value); }

    // -----------------------------------

    /** An array of {@link XrSystemSpaceWarpPropertiesFB} structs. */
    public static class Buffer extends StructBuffer<XrSystemSpaceWarpPropertiesFB, Buffer> implements NativeResource {

        private static final XrSystemSpaceWarpPropertiesFB ELEMENT_FACTORY = XrSystemSpaceWarpPropertiesFB.create(-1L);

        /**
         * Creates a new {@code XrSystemSpaceWarpPropertiesFB.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrSystemSpaceWarpPropertiesFB#SIZEOF}, and its mark will be undefined.
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
        protected XrSystemSpaceWarpPropertiesFB getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrSystemSpaceWarpPropertiesFB.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrSystemSpaceWarpPropertiesFB.nnext(address()); }
        /** @return the value of the {@code recommendedMotionVectorImageRectWidth} field. */
        @NativeType("uint32_t")
        public int recommendedMotionVectorImageRectWidth() { return XrSystemSpaceWarpPropertiesFB.nrecommendedMotionVectorImageRectWidth(address()); }
        /** @return the value of the {@code recommendedMotionVectorImageRectHeight} field. */
        @NativeType("uint32_t")
        public int recommendedMotionVectorImageRectHeight() { return XrSystemSpaceWarpPropertiesFB.nrecommendedMotionVectorImageRectHeight(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrSystemSpaceWarpPropertiesFB.ntype(address(), value); return this; }
        /** Sets the {@link FBSpaceWarp#XR_TYPE_SYSTEM_SPACE_WARP_PROPERTIES_FB TYPE_SYSTEM_SPACE_WARP_PROPERTIES_FB} value to the {@code type} field. */
        public Buffer type$Default() { return type(FBSpaceWarp.XR_TYPE_SYSTEM_SPACE_WARP_PROPERTIES_FB); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrSystemSpaceWarpPropertiesFB.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code recommendedMotionVectorImageRectWidth} field. */
        public Buffer recommendedMotionVectorImageRectWidth(@NativeType("uint32_t") int value) { XrSystemSpaceWarpPropertiesFB.nrecommendedMotionVectorImageRectWidth(address(), value); return this; }
        /** Sets the specified value to the {@code recommendedMotionVectorImageRectHeight} field. */
        public Buffer recommendedMotionVectorImageRectHeight(@NativeType("uint32_t") int value) { XrSystemSpaceWarpPropertiesFB.nrecommendedMotionVectorImageRectHeight(address(), value); return this; }

    }

}