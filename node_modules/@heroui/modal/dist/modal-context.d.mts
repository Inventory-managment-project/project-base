import * as _heroui_theme from '@heroui/theme';
import * as framer_motion from 'framer-motion';
import * as react from 'react';
import * as tailwind_variants from 'tailwind-variants';
import * as _heroui_system from '@heroui/system';

declare const ModalProvider: react.Provider<{
    Component: _heroui_system.As<any>;
    slots: {
        wrapper: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        base: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        backdrop: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        header: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        body: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        footer: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        closeButton: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {
        wrapper: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        base: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        backdrop: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        header: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        body: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        footer: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        closeButton: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {};
    domRef: react.RefObject<HTMLElement>;
    headerId: string;
    bodyId: string;
    motionProps: framer_motion.HTMLMotionProps<"section"> | undefined;
    classNames: _heroui_theme.SlotsToClasses<"base" | "body" | "footer" | "header" | "wrapper" | "backdrop" | "closeButton"> | undefined;
    isDismissable: boolean;
    closeButton: react.ReactNode;
    hideCloseButton: boolean;
    portalContainer: Element | undefined;
    shouldBlockScroll: boolean;
    backdrop: "blur" | "transparent" | "opaque";
    isOpen: boolean;
    onClose: () => void;
    disableAnimation: boolean;
    setBodyMounted: react.Dispatch<react.SetStateAction<boolean>>;
    setHeaderMounted: react.Dispatch<react.SetStateAction<boolean>>;
    getDialogProps: _heroui_system.PropGetter;
    getBackdropProps: _heroui_system.PropGetter;
    getCloseButtonProps: _heroui_system.PropGetter;
}>;
declare const useModalContext: () => {
    Component: _heroui_system.As<any>;
    slots: {
        wrapper: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        base: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        backdrop: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        header: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        body: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        footer: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        closeButton: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {
        wrapper: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        base: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        backdrop: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        header: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        body: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        footer: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        closeButton: (slotProps?: ({
            size?: "sm" | "md" | "lg" | "xl" | "2xl" | "full" | "xs" | "3xl" | "4xl" | "5xl" | undefined;
            disableAnimation?: boolean | undefined;
            shadow?: "none" | "sm" | "md" | "lg" | undefined;
            radius?: "none" | "sm" | "md" | "lg" | undefined;
            backdrop?: "blur" | "transparent" | "opaque" | undefined;
            placement?: "center" | "bottom" | "top" | "auto" | "top-center" | "bottom-center" | undefined;
            scrollBehavior?: "inside" | "outside" | "normal" | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {};
    domRef: react.RefObject<HTMLElement>;
    headerId: string;
    bodyId: string;
    motionProps: framer_motion.HTMLMotionProps<"section"> | undefined;
    classNames: _heroui_theme.SlotsToClasses<"base" | "body" | "footer" | "header" | "wrapper" | "backdrop" | "closeButton"> | undefined;
    isDismissable: boolean;
    closeButton: react.ReactNode;
    hideCloseButton: boolean;
    portalContainer: Element | undefined;
    shouldBlockScroll: boolean;
    backdrop: "blur" | "transparent" | "opaque";
    isOpen: boolean;
    onClose: () => void;
    disableAnimation: boolean;
    setBodyMounted: react.Dispatch<react.SetStateAction<boolean>>;
    setHeaderMounted: react.Dispatch<react.SetStateAction<boolean>>;
    getDialogProps: _heroui_system.PropGetter;
    getBackdropProps: _heroui_system.PropGetter;
    getCloseButtonProps: _heroui_system.PropGetter;
};

export { ModalProvider, useModalContext };
