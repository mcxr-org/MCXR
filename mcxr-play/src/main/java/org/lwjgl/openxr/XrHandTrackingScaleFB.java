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
 * struct XrHandTrackingScaleFB {
 *     XrStructureType type;
 *     void * next;
 *     float sensorOutput;
 *     float currentOutput;
 *     XrBool32 overrideHandScale;
 *     float overrideValueInput;
 * }</code></pre>
 */
public class XrHandTrackingScaleFB extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        SENSOROUTPUT,
        CURRENTOUTPUT,
        OVERRIDEHANDSCALE,
        OVERRIDEVALUEINPUT;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(4),
            __member(4),
            __member(4),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        SENSOROUTPUT = layout.offsetof(2);
        CURRENTOUTPUT = layout.offsetof(3);
        OVERRIDEHANDSCALE = layout.offsetof(4);
        OVERRIDEVALUEINPUT = layout.offsetof(5);
    }

    /**
     * Creates a {@code XrHandTrackingScaleFB} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrHandTrackingScaleFB(ByteBuffer container) {
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
    /** @return the value of the {@code sensorOutput} field. */
    public float sensorOutput() { return nsensorOutput(address()); }
    /** @return the value of the {@code currentOutput} field. */
    public float currentOutput() { return ncurrentOutput(address()); }
    /** @return the value of the {@code overrideHandScale} field. */
    @NativeType("XrBool32")
    public boolean overrideHandScale() { return noverrideHandScale(address()) != 0; }
    /** @return the value of the {@code overrideValueInput} field. */
    public float overrideValueInput() { return noverrideValueInput(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrHandTrackingScaleFB type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link FBHandTrackingMesh#XR_TYPE_HAND_TRACKING_SCALE_FB TYPE_HAND_TRACKING_SCALE_FB} value to the {@code type} field. */
    public XrHandTrackingScaleFB type$Default() { return type(FBHandTrackingMesh.XR_TYPE_HAND_TRACKING_SCALE_FB); }
    /** Sets the specified value to the {@code next} field. */
    public XrHandTrackingScaleFB next(@NativeType("void *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code sensorOutput} field. */
    public XrHandTrackingScaleFB sensorOutput(float value) { nsensorOutput(address(), value); return this; }
    /** Sets the specified value to the {@code currentOutput} field. */
    public XrHandTrackingScaleFB currentOutput(float value) { ncurrentOutput(address(), value); return this; }
    /** Sets the specified value to the {@code overrideHandScale} field. */
    public XrHandTrackingScaleFB overrideHandScale(@NativeType("XrBool32") boolean value) { noverrideHandScale(address(), value ? 1 : 0); return this; }
    /** Sets the specified value to the {@code overrideValueInput} field. */
    public XrHandTrackingScaleFB overrideValueInput(float value) { noverrideValueInput(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrHandTrackingScaleFB set(
        int type,
        long next,
        float sensorOutput,
        float currentOutput,
        boolean overrideHandScale,
        float overrideValueInput
    ) {
        type(type);
        next(next);
        sensorOutput(sensorOutput);
        currentOutput(currentOutput);
        overrideHandScale(overrideHandScale);
        overrideValueInput(overrideValueInput);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrHandTrackingScaleFB set(XrHandTrackingScaleFB src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrHandTrackingScaleFB} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrHandTrackingScaleFB malloc() {
        return wrap(XrHandTrackingScaleFB.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrHandTrackingScaleFB} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrHandTrackingScaleFB calloc() {
        return wrap(XrHandTrackingScaleFB.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrHandTrackingScaleFB} instance allocated with {@link BufferUtils}. */
    public static XrHandTrackingScaleFB create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrHandTrackingScaleFB.class, memAddress(container), container);
    }

    /** Returns a new {@code XrHandTrackingScaleFB} instance for the specified memory address. */
    public static XrHandTrackingScaleFB create(long address) {
        return wrap(XrHandTrackingScaleFB.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrHandTrackingScaleFB createSafe(long address) {
        return address == NULL ? null : wrap(XrHandTrackingScaleFB.class, address);
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
    public static XrHandTrackingScaleFB.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrHandTrackingScaleFB} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrHandTrackingScaleFB malloc(MemoryStack stack) {
        return wrap(XrHandTrackingScaleFB.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrHandTrackingScaleFB} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrHandTrackingScaleFB calloc(MemoryStack stack) {
        return wrap(XrHandTrackingScaleFB.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrHandTrackingScaleFB.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrHandTrackingScaleFB.NEXT); }
    /** Unsafe version of {@link #sensorOutput}. */
    public static float nsensorOutput(long struct) { return UNSAFE.getFloat(null, struct + XrHandTrackingScaleFB.SENSOROUTPUT); }
    /** Unsafe version of {@link #currentOutput}. */
    public static float ncurrentOutput(long struct) { return UNSAFE.getFloat(null, struct + XrHandTrackingScaleFB.CURRENTOUTPUT); }
    /** Unsafe version of {@link #overrideHandScale}. */
    public static int noverrideHandScale(long struct) { return UNSAFE.getInt(null, struct + XrHandTrackingScaleFB.OVERRIDEHANDSCALE); }
    /** Unsafe version of {@link #overrideValueInput}. */
    public static float noverrideValueInput(long struct) { return UNSAFE.getFloat(null, struct + XrHandTrackingScaleFB.OVERRIDEVALUEINPUT); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrHandTrackingScaleFB.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrHandTrackingScaleFB.NEXT, value); }
    /** Unsafe version of {@link #sensorOutput(float) sensorOutput}. */
    public static void nsensorOutput(long struct, float value) { UNSAFE.putFloat(null, struct + XrHandTrackingScaleFB.SENSOROUTPUT, value); }
    /** Unsafe version of {@link #currentOutput(float) currentOutput}. */
    public static void ncurrentOutput(long struct, float value) { UNSAFE.putFloat(null, struct + XrHandTrackingScaleFB.CURRENTOUTPUT, value); }
    /** Unsafe version of {@link #overrideHandScale(boolean) overrideHandScale}. */
    public static void noverrideHandScale(long struct, int value) { UNSAFE.putInt(null, struct + XrHandTrackingScaleFB.OVERRIDEHANDSCALE, value); }
    /** Unsafe version of {@link #overrideValueInput(float) overrideValueInput}. */
    public static void noverrideValueInput(long struct, float value) { UNSAFE.putFloat(null, struct + XrHandTrackingScaleFB.OVERRIDEVALUEINPUT, value); }

    // -----------------------------------

    /** An array of {@link XrHandTrackingScaleFB} structs. */
    public static class Buffer extends StructBuffer<XrHandTrackingScaleFB, Buffer> implements NativeResource {

        private static final XrHandTrackingScaleFB ELEMENT_FACTORY = XrHandTrackingScaleFB.create(-1L);

        /**
         * Creates a new {@code XrHandTrackingScaleFB.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrHandTrackingScaleFB#SIZEOF}, and its mark will be undefined.
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
        protected XrHandTrackingScaleFB getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrHandTrackingScaleFB.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrHandTrackingScaleFB.nnext(address()); }
        /** @return the value of the {@code sensorOutput} field. */
        public float sensorOutput() { return XrHandTrackingScaleFB.nsensorOutput(address()); }
        /** @return the value of the {@code currentOutput} field. */
        public float currentOutput() { return XrHandTrackingScaleFB.ncurrentOutput(address()); }
        /** @return the value of the {@code overrideHandScale} field. */
        @NativeType("XrBool32")
        public boolean overrideHandScale() { return XrHandTrackingScaleFB.noverrideHandScale(address()) != 0; }
        /** @return the value of the {@code overrideValueInput} field. */
        public float overrideValueInput() { return XrHandTrackingScaleFB.noverrideValueInput(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrHandTrackingScaleFB.ntype(address(), value); return this; }
        /** Sets the {@link FBHandTrackingMesh#XR_TYPE_HAND_TRACKING_SCALE_FB TYPE_HAND_TRACKING_SCALE_FB} value to the {@code type} field. */
        public Buffer type$Default() { return type(FBHandTrackingMesh.XR_TYPE_HAND_TRACKING_SCALE_FB); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrHandTrackingScaleFB.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code sensorOutput} field. */
        public Buffer sensorOutput(float value) { XrHandTrackingScaleFB.nsensorOutput(address(), value); return this; }
        /** Sets the specified value to the {@code currentOutput} field. */
        public Buffer currentOutput(float value) { XrHandTrackingScaleFB.ncurrentOutput(address(), value); return this; }
        /** Sets the specified value to the {@code overrideHandScale} field. */
        public Buffer overrideHandScale(@NativeType("XrBool32") boolean value) { XrHandTrackingScaleFB.noverrideHandScale(address(), value ? 1 : 0); return this; }
        /** Sets the specified value to the {@code overrideValueInput} field. */
        public Buffer overrideValueInput(float value) { XrHandTrackingScaleFB.noverrideValueInput(address(), value); return this; }

    }

}