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
 * struct XrSceneObjectsMSFT {
 *     XrStructureType type;
 *     void * next;
 *     uint32_t sceneObjectCount;
 *     {@link XrSceneObjectMSFT XrSceneObjectMSFT} * sceneObjects;
 * }</code></pre>
 */
public class XrSceneObjectsMSFT extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        SCENEOBJECTCOUNT,
        SCENEOBJECTS;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(POINTER_SIZE)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        SCENEOBJECTCOUNT = layout.offsetof(2);
        SCENEOBJECTS = layout.offsetof(3);
    }

    /**
     * Creates a {@code XrSceneObjectsMSFT} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrSceneObjectsMSFT(ByteBuffer container) {
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
    /** @return the value of the {@code sceneObjectCount} field. */
    @NativeType("uint32_t")
    public int sceneObjectCount() { return nsceneObjectCount(address()); }
    /** @return a {@link XrSceneObjectMSFT.Buffer} view of the struct array pointed to by the {@code sceneObjects} field. */
    @Nullable
    @NativeType("XrSceneObjectMSFT *")
    public XrSceneObjectMSFT.Buffer sceneObjects() { return nsceneObjects(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrSceneObjectsMSFT type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_SCENE_OBJECTS_MSFT TYPE_SCENE_OBJECTS_MSFT} value to the {@code type} field. */
    public XrSceneObjectsMSFT type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_SCENE_OBJECTS_MSFT); }
    /** Sets the specified value to the {@code next} field. */
    public XrSceneObjectsMSFT next(@NativeType("void *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code sceneObjectCount} field. */
    public XrSceneObjectsMSFT sceneObjectCount(@NativeType("uint32_t") int value) { nsceneObjectCount(address(), value); return this; }
    /** Sets the address of the specified {@link XrSceneObjectMSFT.Buffer} to the {@code sceneObjects} field. */
    public XrSceneObjectsMSFT sceneObjects(@Nullable @NativeType("XrSceneObjectMSFT *") XrSceneObjectMSFT.Buffer value) { nsceneObjects(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrSceneObjectsMSFT set(
        int type,
        long next,
        int sceneObjectCount,
        @Nullable XrSceneObjectMSFT.Buffer sceneObjects
    ) {
        type(type);
        next(next);
        sceneObjectCount(sceneObjectCount);
        sceneObjects(sceneObjects);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrSceneObjectsMSFT set(XrSceneObjectsMSFT src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrSceneObjectsMSFT} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrSceneObjectsMSFT malloc() {
        return wrap(XrSceneObjectsMSFT.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrSceneObjectsMSFT} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrSceneObjectsMSFT calloc() {
        return wrap(XrSceneObjectsMSFT.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrSceneObjectsMSFT} instance allocated with {@link BufferUtils}. */
    public static XrSceneObjectsMSFT create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrSceneObjectsMSFT.class, memAddress(container), container);
    }

    /** Returns a new {@code XrSceneObjectsMSFT} instance for the specified memory address. */
    public static XrSceneObjectsMSFT create(long address) {
        return wrap(XrSceneObjectsMSFT.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrSceneObjectsMSFT createSafe(long address) {
        return address == NULL ? null : wrap(XrSceneObjectsMSFT.class, address);
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
    public static XrSceneObjectsMSFT.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrSceneObjectsMSFT} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSceneObjectsMSFT malloc(MemoryStack stack) {
        return wrap(XrSceneObjectsMSFT.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrSceneObjectsMSFT} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSceneObjectsMSFT calloc(MemoryStack stack) {
        return wrap(XrSceneObjectsMSFT.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrSceneObjectsMSFT.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrSceneObjectsMSFT.NEXT); }
    /** Unsafe version of {@link #sceneObjectCount}. */
    public static int nsceneObjectCount(long struct) { return UNSAFE.getInt(null, struct + XrSceneObjectsMSFT.SCENEOBJECTCOUNT); }
    /** Unsafe version of {@link #sceneObjects}. */
    @Nullable public static XrSceneObjectMSFT.Buffer nsceneObjects(long struct) { return XrSceneObjectMSFT.createSafe(memGetAddress(struct + XrSceneObjectsMSFT.SCENEOBJECTS), nsceneObjectCount(struct)); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrSceneObjectsMSFT.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrSceneObjectsMSFT.NEXT, value); }
    /** Sets the specified value to the {@code sceneObjectCount} field of the specified {@code struct}. */
    public static void nsceneObjectCount(long struct, int value) { UNSAFE.putInt(null, struct + XrSceneObjectsMSFT.SCENEOBJECTCOUNT, value); }
    /** Unsafe version of {@link #sceneObjects(XrSceneObjectMSFT.Buffer) sceneObjects}. */
    public static void nsceneObjects(long struct, @Nullable XrSceneObjectMSFT.Buffer value) { memPutAddress(struct + XrSceneObjectsMSFT.SCENEOBJECTS, memAddressSafe(value)); if (value != null) { nsceneObjectCount(struct, value.remaining()); } }

    // -----------------------------------

    /** An array of {@link XrSceneObjectsMSFT} structs. */
    public static class Buffer extends StructBuffer<XrSceneObjectsMSFT, Buffer> implements NativeResource {

        private static final XrSceneObjectsMSFT ELEMENT_FACTORY = XrSceneObjectsMSFT.create(-1L);

        /**
         * Creates a new {@code XrSceneObjectsMSFT.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrSceneObjectsMSFT#SIZEOF}, and its mark will be undefined.
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
        protected XrSceneObjectsMSFT getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrSceneObjectsMSFT.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrSceneObjectsMSFT.nnext(address()); }
        /** @return the value of the {@code sceneObjectCount} field. */
        @NativeType("uint32_t")
        public int sceneObjectCount() { return XrSceneObjectsMSFT.nsceneObjectCount(address()); }
        /** @return a {@link XrSceneObjectMSFT.Buffer} view of the struct array pointed to by the {@code sceneObjects} field. */
        @Nullable
        @NativeType("XrSceneObjectMSFT *")
        public XrSceneObjectMSFT.Buffer sceneObjects() { return XrSceneObjectsMSFT.nsceneObjects(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrSceneObjectsMSFT.ntype(address(), value); return this; }
        /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_SCENE_OBJECTS_MSFT TYPE_SCENE_OBJECTS_MSFT} value to the {@code type} field. */
        public Buffer type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_SCENE_OBJECTS_MSFT); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrSceneObjectsMSFT.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code sceneObjectCount} field. */
        public Buffer sceneObjectCount(@NativeType("uint32_t") int value) { XrSceneObjectsMSFT.nsceneObjectCount(address(), value); return this; }
        /** Sets the address of the specified {@link XrSceneObjectMSFT.Buffer} to the {@code sceneObjects} field. */
        public Buffer sceneObjects(@Nullable @NativeType("XrSceneObjectMSFT *") XrSceneObjectMSFT.Buffer value) { XrSceneObjectsMSFT.nsceneObjects(address(), value); return this; }

    }

}