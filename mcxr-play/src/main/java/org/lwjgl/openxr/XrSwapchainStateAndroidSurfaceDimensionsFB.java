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
 * struct XrSwapchainStateAndroidSurfaceDimensionsFB {
 *     XrStructureType type;
 *     void * next;
 *     uint32_t width;
 *     uint32_t height;
 * }</code></pre>
 */
public class XrSwapchainStateAndroidSurfaceDimensionsFB extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        WIDTH,
        HEIGHT;

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
        WIDTH = layout.offsetof(2);
        HEIGHT = layout.offsetof(3);
    }

    /**
     * Creates a {@code XrSwapchainStateAndroidSurfaceDimensionsFB} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrSwapchainStateAndroidSurfaceDimensionsFB(ByteBuffer container) {
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
    /** @return the value of the {@code width} field. */
    @NativeType("uint32_t")
    public int width() { return nwidth(address()); }
    /** @return the value of the {@code height} field. */
    @NativeType("uint32_t")
    public int height() { return nheight(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrSwapchainStateAndroidSurfaceDimensionsFB type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link FBSwapchainUpdateStateAndroidSurface#XR_TYPE_SWAPCHAIN_STATE_ANDROID_SURFACE_DIMENSIONS_FB TYPE_SWAPCHAIN_STATE_ANDROID_SURFACE_DIMENSIONS_FB} value to the {@code type} field. */
    public XrSwapchainStateAndroidSurfaceDimensionsFB type$Default() { return type(FBSwapchainUpdateStateAndroidSurface.XR_TYPE_SWAPCHAIN_STATE_ANDROID_SURFACE_DIMENSIONS_FB); }
    /** Sets the specified value to the {@code next} field. */
    public XrSwapchainStateAndroidSurfaceDimensionsFB next(@NativeType("void *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code width} field. */
    public XrSwapchainStateAndroidSurfaceDimensionsFB width(@NativeType("uint32_t") int value) { nwidth(address(), value); return this; }
    /** Sets the specified value to the {@code height} field. */
    public XrSwapchainStateAndroidSurfaceDimensionsFB height(@NativeType("uint32_t") int value) { nheight(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrSwapchainStateAndroidSurfaceDimensionsFB set(
        int type,
        long next,
        int width,
        int height
    ) {
        type(type);
        next(next);
        width(width);
        height(height);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrSwapchainStateAndroidSurfaceDimensionsFB set(XrSwapchainStateAndroidSurfaceDimensionsFB src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrSwapchainStateAndroidSurfaceDimensionsFB} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrSwapchainStateAndroidSurfaceDimensionsFB malloc() {
        return wrap(XrSwapchainStateAndroidSurfaceDimensionsFB.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrSwapchainStateAndroidSurfaceDimensionsFB} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrSwapchainStateAndroidSurfaceDimensionsFB calloc() {
        return wrap(XrSwapchainStateAndroidSurfaceDimensionsFB.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrSwapchainStateAndroidSurfaceDimensionsFB} instance allocated with {@link BufferUtils}. */
    public static XrSwapchainStateAndroidSurfaceDimensionsFB create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrSwapchainStateAndroidSurfaceDimensionsFB.class, memAddress(container), container);
    }

    /** Returns a new {@code XrSwapchainStateAndroidSurfaceDimensionsFB} instance for the specified memory address. */
    public static XrSwapchainStateAndroidSurfaceDimensionsFB create(long address) {
        return wrap(XrSwapchainStateAndroidSurfaceDimensionsFB.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrSwapchainStateAndroidSurfaceDimensionsFB createSafe(long address) {
        return address == NULL ? null : wrap(XrSwapchainStateAndroidSurfaceDimensionsFB.class, address);
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
    public static XrSwapchainStateAndroidSurfaceDimensionsFB.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrSwapchainStateAndroidSurfaceDimensionsFB} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSwapchainStateAndroidSurfaceDimensionsFB malloc(MemoryStack stack) {
        return wrap(XrSwapchainStateAndroidSurfaceDimensionsFB.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrSwapchainStateAndroidSurfaceDimensionsFB} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSwapchainStateAndroidSurfaceDimensionsFB calloc(MemoryStack stack) {
        return wrap(XrSwapchainStateAndroidSurfaceDimensionsFB.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrSwapchainStateAndroidSurfaceDimensionsFB.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrSwapchainStateAndroidSurfaceDimensionsFB.NEXT); }
    /** Unsafe version of {@link #width}. */
    public static int nwidth(long struct) { return UNSAFE.getInt(null, struct + XrSwapchainStateAndroidSurfaceDimensionsFB.WIDTH); }
    /** Unsafe version of {@link #height}. */
    public static int nheight(long struct) { return UNSAFE.getInt(null, struct + XrSwapchainStateAndroidSurfaceDimensionsFB.HEIGHT); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrSwapchainStateAndroidSurfaceDimensionsFB.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrSwapchainStateAndroidSurfaceDimensionsFB.NEXT, value); }
    /** Unsafe version of {@link #width(int) width}. */
    public static void nwidth(long struct, int value) { UNSAFE.putInt(null, struct + XrSwapchainStateAndroidSurfaceDimensionsFB.WIDTH, value); }
    /** Unsafe version of {@link #height(int) height}. */
    public static void nheight(long struct, int value) { UNSAFE.putInt(null, struct + XrSwapchainStateAndroidSurfaceDimensionsFB.HEIGHT, value); }

    // -----------------------------------

    /** An array of {@link XrSwapchainStateAndroidSurfaceDimensionsFB} structs. */
    public static class Buffer extends StructBuffer<XrSwapchainStateAndroidSurfaceDimensionsFB, Buffer> implements NativeResource {

        private static final XrSwapchainStateAndroidSurfaceDimensionsFB ELEMENT_FACTORY = XrSwapchainStateAndroidSurfaceDimensionsFB.create(-1L);

        /**
         * Creates a new {@code XrSwapchainStateAndroidSurfaceDimensionsFB.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrSwapchainStateAndroidSurfaceDimensionsFB#SIZEOF}, and its mark will be undefined.
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
        protected XrSwapchainStateAndroidSurfaceDimensionsFB getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrSwapchainStateAndroidSurfaceDimensionsFB.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrSwapchainStateAndroidSurfaceDimensionsFB.nnext(address()); }
        /** @return the value of the {@code width} field. */
        @NativeType("uint32_t")
        public int width() { return XrSwapchainStateAndroidSurfaceDimensionsFB.nwidth(address()); }
        /** @return the value of the {@code height} field. */
        @NativeType("uint32_t")
        public int height() { return XrSwapchainStateAndroidSurfaceDimensionsFB.nheight(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrSwapchainStateAndroidSurfaceDimensionsFB.ntype(address(), value); return this; }
        /** Sets the {@link FBSwapchainUpdateStateAndroidSurface#XR_TYPE_SWAPCHAIN_STATE_ANDROID_SURFACE_DIMENSIONS_FB TYPE_SWAPCHAIN_STATE_ANDROID_SURFACE_DIMENSIONS_FB} value to the {@code type} field. */
        public Buffer type$Default() { return type(FBSwapchainUpdateStateAndroidSurface.XR_TYPE_SWAPCHAIN_STATE_ANDROID_SURFACE_DIMENSIONS_FB); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrSwapchainStateAndroidSurfaceDimensionsFB.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code width} field. */
        public Buffer width(@NativeType("uint32_t") int value) { XrSwapchainStateAndroidSurfaceDimensionsFB.nwidth(address(), value); return this; }
        /** Sets the specified value to the {@code height} field. */
        public Buffer height(@NativeType("uint32_t") int value) { XrSwapchainStateAndroidSurfaceDimensionsFB.nheight(address(), value); return this; }

    }

}