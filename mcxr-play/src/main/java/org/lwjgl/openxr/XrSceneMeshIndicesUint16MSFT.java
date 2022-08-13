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
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrSceneMeshIndicesUint16MSFT {
 *     XrStructureType type;
 *     void * next;
 *     uint32_t indexCapacityInput;
 *     uint32_t indexCountOutput;
 *     uint16_t * indices;
 * }</code></pre>
 */
public class XrSceneMeshIndicesUint16MSFT extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        INDEXCAPACITYINPUT,
        INDEXCOUNTOUTPUT,
        INDICES;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(4),
            __member(POINTER_SIZE)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        INDEXCAPACITYINPUT = layout.offsetof(2);
        INDEXCOUNTOUTPUT = layout.offsetof(3);
        INDICES = layout.offsetof(4);
    }

    /**
     * Creates a {@code XrSceneMeshIndicesUint16MSFT} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrSceneMeshIndicesUint16MSFT(ByteBuffer container) {
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
    /** @return the value of the {@code indexCapacityInput} field. */
    @NativeType("uint32_t")
    public int indexCapacityInput() { return nindexCapacityInput(address()); }
    /** @return the value of the {@code indexCountOutput} field. */
    @NativeType("uint32_t")
    public int indexCountOutput() { return nindexCountOutput(address()); }
    /** @return a {@link ShortBuffer} view of the data pointed to by the {@code indices} field. */
    @Nullable
    @NativeType("uint16_t *")
    public ShortBuffer indices() { return nindices(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrSceneMeshIndicesUint16MSFT type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_SCENE_MESH_INDICES_UINT16_MSFT TYPE_SCENE_MESH_INDICES_UINT16_MSFT} value to the {@code type} field. */
    public XrSceneMeshIndicesUint16MSFT type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_SCENE_MESH_INDICES_UINT16_MSFT); }
    /** Sets the specified value to the {@code next} field. */
    public XrSceneMeshIndicesUint16MSFT next(@NativeType("void *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code indexCapacityInput} field. */
    public XrSceneMeshIndicesUint16MSFT indexCapacityInput(@NativeType("uint32_t") int value) { nindexCapacityInput(address(), value); return this; }
    /** Sets the specified value to the {@code indexCountOutput} field. */
    public XrSceneMeshIndicesUint16MSFT indexCountOutput(@NativeType("uint32_t") int value) { nindexCountOutput(address(), value); return this; }
    /** Sets the address of the specified {@link ShortBuffer} to the {@code indices} field. */
    public XrSceneMeshIndicesUint16MSFT indices(@Nullable @NativeType("uint16_t *") ShortBuffer value) { nindices(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrSceneMeshIndicesUint16MSFT set(
        int type,
        long next,
        int indexCapacityInput,
        int indexCountOutput,
        @Nullable ShortBuffer indices
    ) {
        type(type);
        next(next);
        indexCapacityInput(indexCapacityInput);
        indexCountOutput(indexCountOutput);
        indices(indices);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrSceneMeshIndicesUint16MSFT set(XrSceneMeshIndicesUint16MSFT src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrSceneMeshIndicesUint16MSFT} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrSceneMeshIndicesUint16MSFT malloc() {
        return wrap(XrSceneMeshIndicesUint16MSFT.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrSceneMeshIndicesUint16MSFT} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrSceneMeshIndicesUint16MSFT calloc() {
        return wrap(XrSceneMeshIndicesUint16MSFT.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrSceneMeshIndicesUint16MSFT} instance allocated with {@link BufferUtils}. */
    public static XrSceneMeshIndicesUint16MSFT create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrSceneMeshIndicesUint16MSFT.class, memAddress(container), container);
    }

    /** Returns a new {@code XrSceneMeshIndicesUint16MSFT} instance for the specified memory address. */
    public static XrSceneMeshIndicesUint16MSFT create(long address) {
        return wrap(XrSceneMeshIndicesUint16MSFT.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrSceneMeshIndicesUint16MSFT createSafe(long address) {
        return address == NULL ? null : wrap(XrSceneMeshIndicesUint16MSFT.class, address);
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
    public static XrSceneMeshIndicesUint16MSFT.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrSceneMeshIndicesUint16MSFT} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSceneMeshIndicesUint16MSFT malloc(MemoryStack stack) {
        return wrap(XrSceneMeshIndicesUint16MSFT.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrSceneMeshIndicesUint16MSFT} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrSceneMeshIndicesUint16MSFT calloc(MemoryStack stack) {
        return wrap(XrSceneMeshIndicesUint16MSFT.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrSceneMeshIndicesUint16MSFT.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrSceneMeshIndicesUint16MSFT.NEXT); }
    /** Unsafe version of {@link #indexCapacityInput}. */
    public static int nindexCapacityInput(long struct) { return UNSAFE.getInt(null, struct + XrSceneMeshIndicesUint16MSFT.INDEXCAPACITYINPUT); }
    /** Unsafe version of {@link #indexCountOutput}. */
    public static int nindexCountOutput(long struct) { return UNSAFE.getInt(null, struct + XrSceneMeshIndicesUint16MSFT.INDEXCOUNTOUTPUT); }
    /** Unsafe version of {@link #indices() indices}. */
    @Nullable public static ShortBuffer nindices(long struct) { return memShortBufferSafe(memGetAddress(struct + XrSceneMeshIndicesUint16MSFT.INDICES), nindexCapacityInput(struct)); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrSceneMeshIndicesUint16MSFT.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrSceneMeshIndicesUint16MSFT.NEXT, value); }
    /** Sets the specified value to the {@code indexCapacityInput} field of the specified {@code struct}. */
    public static void nindexCapacityInput(long struct, int value) { UNSAFE.putInt(null, struct + XrSceneMeshIndicesUint16MSFT.INDEXCAPACITYINPUT, value); }
    /** Unsafe version of {@link #indexCountOutput(int) indexCountOutput}. */
    public static void nindexCountOutput(long struct, int value) { UNSAFE.putInt(null, struct + XrSceneMeshIndicesUint16MSFT.INDEXCOUNTOUTPUT, value); }
    /** Unsafe version of {@link #indices(ShortBuffer) indices}. */
    public static void nindices(long struct, @Nullable ShortBuffer value) { memPutAddress(struct + XrSceneMeshIndicesUint16MSFT.INDICES, memAddressSafe(value)); if (value != null) { nindexCapacityInput(struct, value.remaining()); } }

    // -----------------------------------

    /** An array of {@link XrSceneMeshIndicesUint16MSFT} structs. */
    public static class Buffer extends StructBuffer<XrSceneMeshIndicesUint16MSFT, Buffer> implements NativeResource {

        private static final XrSceneMeshIndicesUint16MSFT ELEMENT_FACTORY = XrSceneMeshIndicesUint16MSFT.create(-1L);

        /**
         * Creates a new {@code XrSceneMeshIndicesUint16MSFT.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrSceneMeshIndicesUint16MSFT#SIZEOF}, and its mark will be undefined.
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
        protected XrSceneMeshIndicesUint16MSFT getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrSceneMeshIndicesUint16MSFT.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrSceneMeshIndicesUint16MSFT.nnext(address()); }
        /** @return the value of the {@code indexCapacityInput} field. */
        @NativeType("uint32_t")
        public int indexCapacityInput() { return XrSceneMeshIndicesUint16MSFT.nindexCapacityInput(address()); }
        /** @return the value of the {@code indexCountOutput} field. */
        @NativeType("uint32_t")
        public int indexCountOutput() { return XrSceneMeshIndicesUint16MSFT.nindexCountOutput(address()); }
        /** @return a {@link ShortBuffer} view of the data pointed to by the {@code indices} field. */
        @Nullable
        @NativeType("uint16_t *")
        public ShortBuffer indices() { return XrSceneMeshIndicesUint16MSFT.nindices(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrSceneMeshIndicesUint16MSFT.ntype(address(), value); return this; }
        /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_SCENE_MESH_INDICES_UINT16_MSFT TYPE_SCENE_MESH_INDICES_UINT16_MSFT} value to the {@code type} field. */
        public Buffer type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_SCENE_MESH_INDICES_UINT16_MSFT); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrSceneMeshIndicesUint16MSFT.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code indexCapacityInput} field. */
        public Buffer indexCapacityInput(@NativeType("uint32_t") int value) { XrSceneMeshIndicesUint16MSFT.nindexCapacityInput(address(), value); return this; }
        /** Sets the specified value to the {@code indexCountOutput} field. */
        public Buffer indexCountOutput(@NativeType("uint32_t") int value) { XrSceneMeshIndicesUint16MSFT.nindexCountOutput(address(), value); return this; }
        /** Sets the address of the specified {@link ShortBuffer} to the {@code indices} field. */
        public Buffer indices(@Nullable @NativeType("uint16_t *") ShortBuffer value) { XrSceneMeshIndicesUint16MSFT.nindices(address(), value); return this; }

    }

}