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
 * struct XrFoveatedViewConfigurationViewVARJO {
 *     XrStructureType type;
 *     void * next;
 *     XrBool32 foveatedRenderingActive;
 * }</code></pre>
 */
public class XrFoveatedViewConfigurationViewVARJO extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        FOVEATEDRENDERINGACTIVE;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        FOVEATEDRENDERINGACTIVE = layout.offsetof(2);
    }

    /**
     * Creates a {@code XrFoveatedViewConfigurationViewVARJO} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrFoveatedViewConfigurationViewVARJO(ByteBuffer container) {
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
    /** @return the value of the {@code foveatedRenderingActive} field. */
    @NativeType("XrBool32")
    public boolean foveatedRenderingActive() { return nfoveatedRenderingActive(address()) != 0; }

    /** Sets the specified value to the {@code type} field. */
    public XrFoveatedViewConfigurationViewVARJO type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link VARJOFoveatedRendering#XR_TYPE_FOVEATED_VIEW_CONFIGURATION_VIEW_VARJO TYPE_FOVEATED_VIEW_CONFIGURATION_VIEW_VARJO} value to the {@code type} field. */
    public XrFoveatedViewConfigurationViewVARJO type$Default() { return type(VARJOFoveatedRendering.XR_TYPE_FOVEATED_VIEW_CONFIGURATION_VIEW_VARJO); }
    /** Sets the specified value to the {@code next} field. */
    public XrFoveatedViewConfigurationViewVARJO next(@NativeType("void *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code foveatedRenderingActive} field. */
    public XrFoveatedViewConfigurationViewVARJO foveatedRenderingActive(@NativeType("XrBool32") boolean value) { nfoveatedRenderingActive(address(), value ? 1 : 0); return this; }

    /** Initializes this struct with the specified values. */
    public XrFoveatedViewConfigurationViewVARJO set(
        int type,
        long next,
        boolean foveatedRenderingActive
    ) {
        type(type);
        next(next);
        foveatedRenderingActive(foveatedRenderingActive);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrFoveatedViewConfigurationViewVARJO set(XrFoveatedViewConfigurationViewVARJO src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrFoveatedViewConfigurationViewVARJO} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrFoveatedViewConfigurationViewVARJO malloc() {
        return wrap(XrFoveatedViewConfigurationViewVARJO.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrFoveatedViewConfigurationViewVARJO} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrFoveatedViewConfigurationViewVARJO calloc() {
        return wrap(XrFoveatedViewConfigurationViewVARJO.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrFoveatedViewConfigurationViewVARJO} instance allocated with {@link BufferUtils}. */
    public static XrFoveatedViewConfigurationViewVARJO create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrFoveatedViewConfigurationViewVARJO.class, memAddress(container), container);
    }

    /** Returns a new {@code XrFoveatedViewConfigurationViewVARJO} instance for the specified memory address. */
    public static XrFoveatedViewConfigurationViewVARJO create(long address) {
        return wrap(XrFoveatedViewConfigurationViewVARJO.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrFoveatedViewConfigurationViewVARJO createSafe(long address) {
        return address == NULL ? null : wrap(XrFoveatedViewConfigurationViewVARJO.class, address);
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
    public static XrFoveatedViewConfigurationViewVARJO.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrFoveatedViewConfigurationViewVARJO} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrFoveatedViewConfigurationViewVARJO malloc(MemoryStack stack) {
        return wrap(XrFoveatedViewConfigurationViewVARJO.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrFoveatedViewConfigurationViewVARJO} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrFoveatedViewConfigurationViewVARJO calloc(MemoryStack stack) {
        return wrap(XrFoveatedViewConfigurationViewVARJO.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrFoveatedViewConfigurationViewVARJO.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrFoveatedViewConfigurationViewVARJO.NEXT); }
    /** Unsafe version of {@link #foveatedRenderingActive}. */
    public static int nfoveatedRenderingActive(long struct) { return UNSAFE.getInt(null, struct + XrFoveatedViewConfigurationViewVARJO.FOVEATEDRENDERINGACTIVE); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrFoveatedViewConfigurationViewVARJO.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrFoveatedViewConfigurationViewVARJO.NEXT, value); }
    /** Unsafe version of {@link #foveatedRenderingActive(boolean) foveatedRenderingActive}. */
    public static void nfoveatedRenderingActive(long struct, int value) { UNSAFE.putInt(null, struct + XrFoveatedViewConfigurationViewVARJO.FOVEATEDRENDERINGACTIVE, value); }

    // -----------------------------------

    /** An array of {@link XrFoveatedViewConfigurationViewVARJO} structs. */
    public static class Buffer extends StructBuffer<XrFoveatedViewConfigurationViewVARJO, Buffer> implements NativeResource {

        private static final XrFoveatedViewConfigurationViewVARJO ELEMENT_FACTORY = XrFoveatedViewConfigurationViewVARJO.create(-1L);

        /**
         * Creates a new {@code XrFoveatedViewConfigurationViewVARJO.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrFoveatedViewConfigurationViewVARJO#SIZEOF}, and its mark will be undefined.
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
        protected XrFoveatedViewConfigurationViewVARJO getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrFoveatedViewConfigurationViewVARJO.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrFoveatedViewConfigurationViewVARJO.nnext(address()); }
        /** @return the value of the {@code foveatedRenderingActive} field. */
        @NativeType("XrBool32")
        public boolean foveatedRenderingActive() { return XrFoveatedViewConfigurationViewVARJO.nfoveatedRenderingActive(address()) != 0; }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrFoveatedViewConfigurationViewVARJO.ntype(address(), value); return this; }
        /** Sets the {@link VARJOFoveatedRendering#XR_TYPE_FOVEATED_VIEW_CONFIGURATION_VIEW_VARJO TYPE_FOVEATED_VIEW_CONFIGURATION_VIEW_VARJO} value to the {@code type} field. */
        public Buffer type$Default() { return type(VARJOFoveatedRendering.XR_TYPE_FOVEATED_VIEW_CONFIGURATION_VIEW_VARJO); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrFoveatedViewConfigurationViewVARJO.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code foveatedRenderingActive} field. */
        public Buffer foveatedRenderingActive(@NativeType("XrBool32") boolean value) { XrFoveatedViewConfigurationViewVARJO.nfoveatedRenderingActive(address(), value ? 1 : 0); return this; }

    }

}