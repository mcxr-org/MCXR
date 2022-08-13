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
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrScenePlaneAlignmentFilterInfoMSFT {
 *     XrStructureType type;
 *     void const * next;
 *     uint32_t alignmentCount;
 *     XrScenePlaneAlignmentTypeMSFT const * alignments;
 * }</code></pre>
 */
public class XrScenePlaneAlignmentFilterInfoMSFT extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        ALIGNMENTCOUNT,
        ALIGNMENTS;

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
        ALIGNMENTCOUNT = layout.offsetof(2);
        ALIGNMENTS = layout.offsetof(3);
    }

    /**
     * Creates a {@code XrScenePlaneAlignmentFilterInfoMSFT} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrScenePlaneAlignmentFilterInfoMSFT(ByteBuffer container) {
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
    /** @return the value of the {@code alignmentCount} field. */
    @NativeType("uint32_t")
    public int alignmentCount() { return nalignmentCount(address()); }
    /** @return a {@link IntBuffer} view of the data pointed to by the {@code alignments} field. */
    @Nullable
    @NativeType("XrScenePlaneAlignmentTypeMSFT const *")
    public IntBuffer alignments() { return nalignments(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrScenePlaneAlignmentFilterInfoMSFT type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT} value to the {@code type} field. */
    public XrScenePlaneAlignmentFilterInfoMSFT type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT); }
    /** Sets the specified value to the {@code next} field. */
    public XrScenePlaneAlignmentFilterInfoMSFT next(@NativeType("void const *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code alignmentCount} field. */
    public XrScenePlaneAlignmentFilterInfoMSFT alignmentCount(@NativeType("uint32_t") int value) { nalignmentCount(address(), value); return this; }
    /** Sets the address of the specified {@link IntBuffer} to the {@code alignments} field. */
    public XrScenePlaneAlignmentFilterInfoMSFT alignments(@Nullable @NativeType("XrScenePlaneAlignmentTypeMSFT const *") IntBuffer value) { nalignments(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrScenePlaneAlignmentFilterInfoMSFT set(
        int type,
        long next,
        int alignmentCount,
        @Nullable IntBuffer alignments
    ) {
        type(type);
        next(next);
        alignmentCount(alignmentCount);
        alignments(alignments);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrScenePlaneAlignmentFilterInfoMSFT set(XrScenePlaneAlignmentFilterInfoMSFT src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrScenePlaneAlignmentFilterInfoMSFT} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrScenePlaneAlignmentFilterInfoMSFT malloc() {
        return wrap(XrScenePlaneAlignmentFilterInfoMSFT.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrScenePlaneAlignmentFilterInfoMSFT} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrScenePlaneAlignmentFilterInfoMSFT calloc() {
        return wrap(XrScenePlaneAlignmentFilterInfoMSFT.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrScenePlaneAlignmentFilterInfoMSFT} instance allocated with {@link BufferUtils}. */
    public static XrScenePlaneAlignmentFilterInfoMSFT create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrScenePlaneAlignmentFilterInfoMSFT.class, memAddress(container), container);
    }

    /** Returns a new {@code XrScenePlaneAlignmentFilterInfoMSFT} instance for the specified memory address. */
    public static XrScenePlaneAlignmentFilterInfoMSFT create(long address) {
        return wrap(XrScenePlaneAlignmentFilterInfoMSFT.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrScenePlaneAlignmentFilterInfoMSFT createSafe(long address) {
        return address == NULL ? null : wrap(XrScenePlaneAlignmentFilterInfoMSFT.class, address);
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
    public static XrScenePlaneAlignmentFilterInfoMSFT.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrScenePlaneAlignmentFilterInfoMSFT} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrScenePlaneAlignmentFilterInfoMSFT malloc(MemoryStack stack) {
        return wrap(XrScenePlaneAlignmentFilterInfoMSFT.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrScenePlaneAlignmentFilterInfoMSFT} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrScenePlaneAlignmentFilterInfoMSFT calloc(MemoryStack stack) {
        return wrap(XrScenePlaneAlignmentFilterInfoMSFT.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrScenePlaneAlignmentFilterInfoMSFT.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrScenePlaneAlignmentFilterInfoMSFT.NEXT); }
    /** Unsafe version of {@link #alignmentCount}. */
    public static int nalignmentCount(long struct) { return UNSAFE.getInt(null, struct + XrScenePlaneAlignmentFilterInfoMSFT.ALIGNMENTCOUNT); }
    /** Unsafe version of {@link #alignments() alignments}. */
    @Nullable public static IntBuffer nalignments(long struct) { return memIntBufferSafe(memGetAddress(struct + XrScenePlaneAlignmentFilterInfoMSFT.ALIGNMENTS), nalignmentCount(struct)); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrScenePlaneAlignmentFilterInfoMSFT.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrScenePlaneAlignmentFilterInfoMSFT.NEXT, value); }
    /** Sets the specified value to the {@code alignmentCount} field of the specified {@code struct}. */
    public static void nalignmentCount(long struct, int value) { UNSAFE.putInt(null, struct + XrScenePlaneAlignmentFilterInfoMSFT.ALIGNMENTCOUNT, value); }
    /** Unsafe version of {@link #alignments(IntBuffer) alignments}. */
    public static void nalignments(long struct, @Nullable IntBuffer value) { memPutAddress(struct + XrScenePlaneAlignmentFilterInfoMSFT.ALIGNMENTS, memAddressSafe(value)); if (value != null) { nalignmentCount(struct, value.remaining()); } }

    // -----------------------------------

    /** An array of {@link XrScenePlaneAlignmentFilterInfoMSFT} structs. */
    public static class Buffer extends StructBuffer<XrScenePlaneAlignmentFilterInfoMSFT, Buffer> implements NativeResource {

        private static final XrScenePlaneAlignmentFilterInfoMSFT ELEMENT_FACTORY = XrScenePlaneAlignmentFilterInfoMSFT.create(-1L);

        /**
         * Creates a new {@code XrScenePlaneAlignmentFilterInfoMSFT.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrScenePlaneAlignmentFilterInfoMSFT#SIZEOF}, and its mark will be undefined.
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
        protected XrScenePlaneAlignmentFilterInfoMSFT getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrScenePlaneAlignmentFilterInfoMSFT.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void const *")
        public long next() { return XrScenePlaneAlignmentFilterInfoMSFT.nnext(address()); }
        /** @return the value of the {@code alignmentCount} field. */
        @NativeType("uint32_t")
        public int alignmentCount() { return XrScenePlaneAlignmentFilterInfoMSFT.nalignmentCount(address()); }
        /** @return a {@link IntBuffer} view of the data pointed to by the {@code alignments} field. */
        @Nullable
        @NativeType("XrScenePlaneAlignmentTypeMSFT const *")
        public IntBuffer alignments() { return XrScenePlaneAlignmentFilterInfoMSFT.nalignments(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrScenePlaneAlignmentFilterInfoMSFT.ntype(address(), value); return this; }
        /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT} value to the {@code type} field. */
        public Buffer type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_SCENE_PLANE_ALIGNMENT_FILTER_INFO_MSFT); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void const *") long value) { XrScenePlaneAlignmentFilterInfoMSFT.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code alignmentCount} field. */
        public Buffer alignmentCount(@NativeType("uint32_t") int value) { XrScenePlaneAlignmentFilterInfoMSFT.nalignmentCount(address(), value); return this; }
        /** Sets the address of the specified {@link IntBuffer} to the {@code alignments} field. */
        public Buffer alignments(@Nullable @NativeType("XrScenePlaneAlignmentTypeMSFT const *") IntBuffer value) { XrScenePlaneAlignmentFilterInfoMSFT.nalignments(address(), value); return this; }

    }

}