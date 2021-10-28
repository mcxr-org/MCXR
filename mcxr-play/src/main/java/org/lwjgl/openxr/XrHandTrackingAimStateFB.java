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
 * struct XrHandTrackingAimStateFB {
 *     XrStructureType type;
 *     void * next;
 *     XrHandTrackingAimFlagsFB status;
 *     {@link XrPosef XrPosef} aimPose;
 *     float pinchStrengthIndex;
 *     float pinchStrengthMiddle;
 *     float pinchStrengthRing;
 *     float pinchStrengthLittle;
 * }</code></pre>
 */
public class XrHandTrackingAimStateFB extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        STATUS,
        AIMPOSE,
        PINCHSTRENGTHINDEX,
        PINCHSTRENGTHMIDDLE,
        PINCHSTRENGTHRING,
        PINCHSTRENGTHLITTLE;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(8),
            __member(XrPosef.SIZEOF, XrPosef.ALIGNOF),
            __member(4),
            __member(4),
            __member(4),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        STATUS = layout.offsetof(2);
        AIMPOSE = layout.offsetof(3);
        PINCHSTRENGTHINDEX = layout.offsetof(4);
        PINCHSTRENGTHMIDDLE = layout.offsetof(5);
        PINCHSTRENGTHRING = layout.offsetof(6);
        PINCHSTRENGTHLITTLE = layout.offsetof(7);
    }

    /**
     * Creates a {@code XrHandTrackingAimStateFB} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrHandTrackingAimStateFB(ByteBuffer container) {
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
    /** @return the value of the {@code status} field. */
    @NativeType("XrHandTrackingAimFlagsFB")
    public long status() { return nstatus(address()); }
    /** @return a {@link XrPosef} view of the {@code aimPose} field. */
    public XrPosef aimPose() { return naimPose(address()); }
    /** @return the value of the {@code pinchStrengthIndex} field. */
    public float pinchStrengthIndex() { return npinchStrengthIndex(address()); }
    /** @return the value of the {@code pinchStrengthMiddle} field. */
    public float pinchStrengthMiddle() { return npinchStrengthMiddle(address()); }
    /** @return the value of the {@code pinchStrengthRing} field. */
    public float pinchStrengthRing() { return npinchStrengthRing(address()); }
    /** @return the value of the {@code pinchStrengthLittle} field. */
    public float pinchStrengthLittle() { return npinchStrengthLittle(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrHandTrackingAimStateFB type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link FBHandTrackingAim#XR_TYPE_HAND_TRACKING_AIM_STATE_FB TYPE_HAND_TRACKING_AIM_STATE_FB} value to the {@code type} field. */
    public XrHandTrackingAimStateFB type$Default() { return type(FBHandTrackingAim.XR_TYPE_HAND_TRACKING_AIM_STATE_FB); }
    /** Sets the specified value to the {@code next} field. */
    public XrHandTrackingAimStateFB next(@NativeType("void *") long value) { nnext(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrHandTrackingAimStateFB set(
        int type,
        long next
    ) {
        type(type);
        next(next);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrHandTrackingAimStateFB set(XrHandTrackingAimStateFB src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrHandTrackingAimStateFB} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrHandTrackingAimStateFB malloc() {
        return wrap(XrHandTrackingAimStateFB.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrHandTrackingAimStateFB} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrHandTrackingAimStateFB calloc() {
        return wrap(XrHandTrackingAimStateFB.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrHandTrackingAimStateFB} instance allocated with {@link BufferUtils}. */
    public static XrHandTrackingAimStateFB create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrHandTrackingAimStateFB.class, memAddress(container), container);
    }

    /** Returns a new {@code XrHandTrackingAimStateFB} instance for the specified memory address. */
    public static XrHandTrackingAimStateFB create(long address) {
        return wrap(XrHandTrackingAimStateFB.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrHandTrackingAimStateFB createSafe(long address) {
        return address == NULL ? null : wrap(XrHandTrackingAimStateFB.class, address);
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
    public static XrHandTrackingAimStateFB.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrHandTrackingAimStateFB} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrHandTrackingAimStateFB malloc(MemoryStack stack) {
        return wrap(XrHandTrackingAimStateFB.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrHandTrackingAimStateFB} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrHandTrackingAimStateFB calloc(MemoryStack stack) {
        return wrap(XrHandTrackingAimStateFB.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
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
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrHandTrackingAimStateFB.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrHandTrackingAimStateFB.NEXT); }
    /** Unsafe version of {@link #status}. */
    public static long nstatus(long struct) { return UNSAFE.getLong(null, struct + XrHandTrackingAimStateFB.STATUS); }
    /** Unsafe version of {@link #aimPose}. */
    public static XrPosef naimPose(long struct) { return XrPosef.create(struct + XrHandTrackingAimStateFB.AIMPOSE); }
    /** Unsafe version of {@link #pinchStrengthIndex}. */
    public static float npinchStrengthIndex(long struct) { return UNSAFE.getFloat(null, struct + XrHandTrackingAimStateFB.PINCHSTRENGTHINDEX); }
    /** Unsafe version of {@link #pinchStrengthMiddle}. */
    public static float npinchStrengthMiddle(long struct) { return UNSAFE.getFloat(null, struct + XrHandTrackingAimStateFB.PINCHSTRENGTHMIDDLE); }
    /** Unsafe version of {@link #pinchStrengthRing}. */
    public static float npinchStrengthRing(long struct) { return UNSAFE.getFloat(null, struct + XrHandTrackingAimStateFB.PINCHSTRENGTHRING); }
    /** Unsafe version of {@link #pinchStrengthLittle}. */
    public static float npinchStrengthLittle(long struct) { return UNSAFE.getFloat(null, struct + XrHandTrackingAimStateFB.PINCHSTRENGTHLITTLE); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrHandTrackingAimStateFB.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrHandTrackingAimStateFB.NEXT, value); }

    // -----------------------------------

    /** An array of {@link XrHandTrackingAimStateFB} structs. */
    public static class Buffer extends StructBuffer<XrHandTrackingAimStateFB, Buffer> implements NativeResource {

        private static final XrHandTrackingAimStateFB ELEMENT_FACTORY = XrHandTrackingAimStateFB.create(-1L);

        /**
         * Creates a new {@code XrHandTrackingAimStateFB.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrHandTrackingAimStateFB#SIZEOF}, and its mark will be undefined.
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
        protected XrHandTrackingAimStateFB getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrHandTrackingAimStateFB.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void *")
        public long next() { return XrHandTrackingAimStateFB.nnext(address()); }
        /** @return the value of the {@code status} field. */
        @NativeType("XrHandTrackingAimFlagsFB")
        public long status() { return XrHandTrackingAimStateFB.nstatus(address()); }
        /** @return a {@link XrPosef} view of the {@code aimPose} field. */
        public XrPosef aimPose() { return XrHandTrackingAimStateFB.naimPose(address()); }
        /** @return the value of the {@code pinchStrengthIndex} field. */
        public float pinchStrengthIndex() { return XrHandTrackingAimStateFB.npinchStrengthIndex(address()); }
        /** @return the value of the {@code pinchStrengthMiddle} field. */
        public float pinchStrengthMiddle() { return XrHandTrackingAimStateFB.npinchStrengthMiddle(address()); }
        /** @return the value of the {@code pinchStrengthRing} field. */
        public float pinchStrengthRing() { return XrHandTrackingAimStateFB.npinchStrengthRing(address()); }
        /** @return the value of the {@code pinchStrengthLittle} field. */
        public float pinchStrengthLittle() { return XrHandTrackingAimStateFB.npinchStrengthLittle(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrHandTrackingAimStateFB.ntype(address(), value); return this; }
        /** Sets the {@link FBHandTrackingAim#XR_TYPE_HAND_TRACKING_AIM_STATE_FB TYPE_HAND_TRACKING_AIM_STATE_FB} value to the {@code type} field. */
        public Buffer type$Default() { return type(FBHandTrackingAim.XR_TYPE_HAND_TRACKING_AIM_STATE_FB); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void *") long value) { XrHandTrackingAimStateFB.nnext(address(), value); return this; }

    }

}