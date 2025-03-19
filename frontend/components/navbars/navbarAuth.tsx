import {
  Navbar as NextUINavbar,
  NavbarContent,
  NavbarBrand,
  NavbarItem,
} from "@heroui/navbar";

import { ThemeSwitch } from "@/components/misc/theme-switch";
import { Logo } from "@/components/misc/icons";
import { Button } from "@heroui/button";
import NextLink from "next/link";

export const Navbar = () => {
  return (
    <NextUINavbar maxWidth="xl" position="sticky">
      <NavbarContent className="basis-1/5 sm:basis-full" justify="end">
        <NavbarBrand as="li" className="gap-3 max-w-fit">
          <NextLink href="/">
          <div className="flex justify-start items-center gap-1">
            <Logo />
            <p className="font-bold text-xl text-inherit tracking-[.25em]">DFC</p>
          </div>
          </NextLink>
        </NavbarBrand>
      </NavbarContent>

      {/*<NavbarContent
        className="hidden sm:flex basis-1/5 sm:basis-full"
        justify="end"
      >
        <NavbarItem className="hidden sm:flex gap-2">
          <ThemeSwitch />
        </NavbarItem>
      </NavbarContent>*/}

      {/*<NavbarContent className="sm:hidden basis-1 pl-4" justify="end">
        <ThemeSwitch />
      </NavbarContent>*/}
    </NextUINavbar>
  );
};
