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

import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrNewSceneComputeInfoMSFT {
 *     XrStructureType type;
 *     void const * next;
 *     uint32_t requestedFeatureCount;
 *     XrSceneComputeFeatureMSFT const * requestedFeatures;
 *     XrSceneComputeConsistencyMSFT consistency;
 *     {@link XrSceneBoundsMSFT XrSceneBoundsMSFT} bounds;
 * }</code></pre>
 */
public class XrNewSceneComputeInfoMSFT extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        REQUESTEDFEATURECOUNT,
        REQUESTEDFEATURES,
        CONSISTENCY,
        BOUNDS;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(XrSceneBoundsMSFT.SIZEOF, XrSceneBoundsMSFT.ALIGNOF)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        REQUESTEDFEATURECOUNT = layout.offsetof(2);
        REQUESTEDFEATURES = layout.offsetof(3);
        CONSISTENCY = layout.offsetof(4);
        BOUNDS = layout.offsetof(5);
    }

    /**
     * Creates a {@code XrNewSceneComputeInfoMSFT} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrNewSceneComputeInfoMSFT(ByteBuffer container) {
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
    /** @return the value of the {@code requestedFeatureCount} field. */
    @NativeType("uint32_t")
    public int requestedFeatureCount() { return nrequestedFeatureCount(address()); }
    /** @return a {@link IntBuffer} view of the data pointed to by the {@code requestedFeatures} field. */
    @NativeType("XrSceneComputeFeatureMSFT const *")
    public IntBuffer requestedFeatures() { return nrequestedFeatures(address()); }
    /** @return the value of the {@code consistency} field. */
    @NativeType("XrSceneComputeConsistencyMSFT")
    public int consistency() { return nconsistency(address()); }
    /** @return a {@link XrSceneBoundsMSFT} view of the {@code bounds} field. */
    public XrSceneBoundsMSFT bounds() { return nbounds(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrNewSceneComputeInfoMSFT type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_NEW_SCENE_COMPUTE_INFO_MSFT TYPE_NEW_SCENE_COMPUTE_INFO_MSFT} value to the {@code type} field. */
    public XrNewSceneComputeInfoMSFT type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_NEW_SCENE_COMPUTE_INFO_MSFT); }
    /** Sets the specified value to the {@code next} field. */
    public XrNewSceneComputeInfoMSFT next(@NativeType("void const *") long value) { nnext(address(), value); return this; }
    /** Sets the address of the specified {@link IntBuffer} to the {@code requestedFeatures} field. */
    public XrNewSceneComputeInfoMSFT requestedFeatures(@NativeType("XrSceneComputeFeatureMSFT const *") IntBuffer value) { nrequestedFeatures(address(), value); return this; }
    /** Sets the specified value to the {@code consistency} field. */
    public XrNewSceneComputeInfoMSFT consistency(@NativeType("XrSceneComputeConsistencyMSFT") int value) { nconsistency(address(), value); return this; }
    /** Copies the specified {@link XrSceneBoundsMSFT} to the {@code bounds} field. */
    public XrNewSceneComputeInfoMSFT bounds(XrSceneBoundsMSFT value) { nbounds(address(), value); return this; }
    /** Passes the {@code bounds} field to the specified {@link java.util.function.Consumer Consumer}. */
    public XrNewSceneComputeInfoMSFT bounds(java.util.function.Consumer<XrSceneBoundsMSFT> consumer) { consumer.accept(bounds()); return this; }

    /** Initializes this struct with the specified values. */
    public XrNewSceneComputeInfoMSFT set(
        int type,
        long next,
        IntBuffer requestedFeatures,
        int consistency,
        XrSceneBoundsMSFT bounds
    ) {
        type(type);
        next(next);
        requestedFeatures(requestedFeatures);
        consistency(consistency);
        bounds(bounds);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrNewSceneComputeInfoMSFT set(XrNewSceneComputeInfoMSFT src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrNewSceneComputeInfoMSFT} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrNewSceneComputeInfoMSFT malloc() {
        return wrap(XrNewSceneComputeInfoMSFT.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrNewSceneComputeInfoMSFT} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrNewSceneComputeInfoMSFT calloc() {
        return wrap(XrNewSceneComputeInfoMSFT.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrNewSceneComputeInfoMSFT} instance allocated with {@link BufferUtils}. */
    public static XrNewSceneComputeInfoMSFT create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrNewSceneComputeInfoMSFT.class, memAddress(container), container);
    }

    /** Returns a new {@code XrNewSceneComputeInfoMSFT} instance for the specified memory address. */
    public static XrNewSceneComputeInfoMSFT create(long address) {
        return wrap(XrNewSceneComputeInfoMSFT.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrNewSceneComputeInfoMSFT createSafe(long address) {
        return address == NULL ? null : wrap(XrNewSceneComputeInfoMSFT.class, address);
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
    public static XrNewSceneComputeInfoMSFT.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrNewSceneComputeInfoMSFT} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrNewSceneComputeInfoMSFT malloc(MemoryStack stack) {
        return wrap(XrNewSceneComputeInfoMSFT.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrNewSceneComputeInfoMSFT} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrNewSceneComputeInfoMSFT calloc(MemoryStack stack) {
        return wrap(XrNewSceneComputeInfoMSFT.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrNewSceneComputeInfoMSFT.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrNewSceneComputeInfoMSFT.NEXT); }
    /** Unsafe version of {@link #requestedFeatureCount}. */
    public static int nrequestedFeatureCount(long struct) { return UNSAFE.getInt(null, struct + XrNewSceneComputeInfoMSFT.REQUESTEDFEATURECOUNT); }
    /** Unsafe version of {@link #requestedFeatures() requestedFeatures}. */
    public static IntBuffer nrequestedFeatures(long struct) { return memIntBuffer(memGetAddress(struct + XrNewSceneComputeInfoMSFT.REQUESTEDFEATURES), nrequestedFeatureCount(struct)); }
    /** Unsafe version of {@link #consistency}. */
    public static int nconsistency(long struct) { return UNSAFE.getInt(null, struct + XrNewSceneComputeInfoMSFT.CONSISTENCY); }
    /** Unsafe version of {@link #bounds}. */
    public static XrSceneBoundsMSFT nbounds(long struct) { return XrSceneBoundsMSFT.create(struct + XrNewSceneComputeInfoMSFT.BOUNDS); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrNewSceneComputeInfoMSFT.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrNewSceneComputeInfoMSFT.NEXT, value); }
    /** Sets the specified value to the {@code requestedFeatureCount} field of the specified {@code struct}. */
    public static void nrequestedFeatureCount(long struct, int value) { UNSAFE.putInt(null, struct + XrNewSceneComputeInfoMSFT.REQUESTEDFEATURECOUNT, value); }
    /** Unsafe version of {@link #requestedFeatures(IntBuffer) requestedFeatures}. */
    public static void nrequestedFeatures(long struct, IntBuffer value) { memPutAddress(struct + XrNewSceneComputeInfoMSFT.REQUESTEDFEATURES, memAddress(value)); nrequestedFeatureCount(struct, value.remaining()); }
    /** Unsafe version of {@link #consistency(int) consistency}. */
    public static void nconsistency(long struct, int value) { UNSAFE.putInt(null, struct + XrNewSceneComputeInfoMSFT.CONSISTENCY, value); }
    /** Unsafe version of {@link #bounds(XrSceneBoundsMSFT) bounds}. */
    public static void nbounds(long struct, XrSceneBoundsMSFT value) { memCopy(value.address(), struct + XrNewSceneComputeInfoMSFT.BOUNDS, XrSceneBoundsMSFT.SIZEOF); }

    /**
     * Validates pointer members that should not be {@code NULL}.
     *
     * @param struct the struct to validate
     */
    public static void validate(long struct) {
        check(memGetAddress(struct + XrNewSceneComputeInfoMSFT.REQUESTEDFEATURES));
        XrSceneBoundsMSFT.validate(struct + XrNewSceneComputeInfoMSFT.BOUNDS);
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

    /** An array of {@link XrNewSceneComputeInfoMSFT} structs. */
    public static class Buffer extends StructBuffer<XrNewSceneComputeInfoMSFT, Buffer> implements NativeResource {

        private static final XrNewSceneComputeInfoMSFT ELEMENT_FACTORY = XrNewSceneComputeInfoMSFT.create(-1L);

        /**
         * Creates a new {@code XrNewSceneComputeInfoMSFT.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrNewSceneComputeInfoMSFT#SIZEOF}, and its mark will be undefined.
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
        protected XrNewSceneComputeInfoMSFT getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrNewSceneComputeInfoMSFT.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void const *")
        public long next() { return XrNewSceneComputeInfoMSFT.nnext(address()); }
        /** @return the value of the {@code requestedFeatureCount} field. */
        @NativeType("uint32_t")
        public int requestedFeatureCount() { return XrNewSceneComputeInfoMSFT.nrequestedFeatureCount(address()); }
        /** @return a {@link IntBuffer} view of the data pointed to by the {@code requestedFeatures} field. */
        @NativeType("XrSceneComputeFeatureMSFT const *")
        public IntBuffer requestedFeatures() { return XrNewSceneComputeInfoMSFT.nrequestedFeatures(address()); }
        /** @return the value of the {@code consistency} field. */
        @NativeType("XrSceneComputeConsistencyMSFT")
        public int consistency() { return XrNewSceneComputeInfoMSFT.nconsistency(address()); }
        /** @return a {@link XrSceneBoundsMSFT} view of the {@code bounds} field. */
        public XrSceneBoundsMSFT bounds() { return XrNewSceneComputeInfoMSFT.nbounds(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrNewSceneComputeInfoMSFT.ntype(address(), value); return this; }
        /** Sets the {@link MSFTSceneUnderstanding#XR_TYPE_NEW_SCENE_COMPUTE_INFO_MSFT TYPE_NEW_SCENE_COMPUTE_INFO_MSFT} value to the {@code type} field. */
        public Buffer type$Default() { return type(MSFTSceneUnderstanding.XR_TYPE_NEW_SCENE_COMPUTE_INFO_MSFT); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void const *") long value) { XrNewSceneComputeInfoMSFT.nnext(address(), value); return this; }
        /** Sets the address of the specified {@link IntBuffer} to the {@code requestedFeatures} field. */
        public Buffer requestedFeatures(@NativeType("XrSceneComputeFeatureMSFT const *") IntBuffer value) { XrNewSceneComputeInfoMSFT.nrequestedFeatures(address(), value); return this; }
        /** Sets the specified value to the {@code consistency} field. */
        public Buffer consistency(@NativeType("XrSceneComputeConsistencyMSFT") int value) { XrNewSceneComputeInfoMSFT.nconsistency(address(), value); return this; }
        /** Copies the specified {@link XrSceneBoundsMSFT} to the {@code bounds} field. */
        public Buffer bounds(XrSceneBoundsMSFT value) { XrNewSceneComputeInfoMSFT.nbounds(address(), value); return this; }
        /** Passes the {@code bounds} field to the specified {@link java.util.function.Consumer Consumer}. */
        public Buffer bounds(java.util.function.Consumer<XrSceneBoundsMSFT> consumer) { consumer.accept(bounds()); return this; }

    }

}