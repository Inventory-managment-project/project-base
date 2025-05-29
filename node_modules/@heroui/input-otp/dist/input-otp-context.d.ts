import * as _heroui_react_utils from '@heroui/react-utils';
import * as input_otp from 'input-otp';
import * as _heroui_theme from '@heroui/theme';
import * as tailwind_variants from 'tailwind-variants';
import * as react from 'react';
import * as _heroui_system from '@heroui/system';

declare const InputOtpProvider: react.Provider<{
    Component: _heroui_system.As<any>;
    inputRef: react.RefObject<HTMLInputElement>;
    length: number;
    value: string;
    type: string | undefined;
    slots: {
        base: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        wrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        input: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segmentWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segment: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        passwordChar: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        caret: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        helperWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        errorMessage: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        description: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {
        base: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        wrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        input: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segmentWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segment: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        passwordChar: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        caret: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        helperWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        errorMessage: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        description: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {};
    hasHelper: boolean;
    classNames: _heroui_theme.SlotsToClasses<"base" | "input" | "description" | "errorMessage" | "wrapper" | "segmentWrapper" | "segment" | "passwordChar" | "caret" | "helperWrapper"> | undefined;
    isInvalid: boolean;
    description: react.ReactNode;
    errorMessage: react.ReactNode;
    isFocusVisible: boolean;
    isFocused: boolean;
    getBaseProps: _heroui_system.PropGetter;
    getInputOtpProps: (props?: Partial<input_otp.OTPInputProps>) => Omit<input_otp.OTPInputProps, "children" | "render"> & {
        ref?: _heroui_react_utils.ReactRef<HTMLInputElement>;
    };
    getSegmentWrapperProps: _heroui_system.PropGetter;
    getHelperWrapperProps: _heroui_system.PropGetter;
    getErrorMessageProps: _heroui_system.PropGetter;
    getDescriptionProps: _heroui_system.PropGetter;
}>;
declare const useInputOtpContext: () => {
    Component: _heroui_system.As<any>;
    inputRef: react.RefObject<HTMLInputElement>;
    length: number;
    value: string;
    type: string | undefined;
    slots: {
        base: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        wrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        input: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segmentWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segment: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        passwordChar: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        caret: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        helperWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        errorMessage: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        description: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {
        base: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        wrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        input: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segmentWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        segment: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        passwordChar: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        caret: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        helperWrapper: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        errorMessage: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
        description: (slotProps?: ({
            color?: "default" | "primary" | "secondary" | "success" | "warning" | "danger" | undefined;
            size?: "sm" | "md" | "lg" | undefined;
            disableAnimation?: boolean | undefined;
            radius?: "none" | "sm" | "md" | "lg" | "full" | undefined;
            isDisabled?: boolean | undefined;
            fullWidth?: boolean | undefined;
            variant?: "flat" | "bordered" | "faded" | "underlined" | undefined;
            isInvalid?: boolean | undefined;
            isReadOnly?: boolean | undefined;
        } & tailwind_variants.ClassProp<tailwind_variants.ClassValue>) | undefined) => string;
    } & {};
    hasHelper: boolean;
    classNames: _heroui_theme.SlotsToClasses<"base" | "input" | "description" | "errorMessage" | "wrapper" | "segmentWrapper" | "segment" | "passwordChar" | "caret" | "helperWrapper"> | undefined;
    isInvalid: boolean;
    description: react.ReactNode;
    errorMessage: react.ReactNode;
    isFocusVisible: boolean;
    isFocused: boolean;
    getBaseProps: _heroui_system.PropGetter;
    getInputOtpProps: (props?: Partial<input_otp.OTPInputProps>) => Omit<input_otp.OTPInputProps, "children" | "render"> & {
        ref?: _heroui_react_utils.ReactRef<HTMLInputElement>;
    };
    getSegmentWrapperProps: _heroui_system.PropGetter;
    getHelperWrapperProps: _heroui_system.PropGetter;
    getErrorMessageProps: _heroui_system.PropGetter;
    getDescriptionProps: _heroui_system.PropGetter;
};

export { InputOtpProvider, useInputOtpContext };
