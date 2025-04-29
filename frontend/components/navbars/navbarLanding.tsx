import {
  Navbar as NextUINavbar,
  NavbarContent,
  NavbarBrand,
  NavbarItem,
} from "@heroui/navbar";

import { ThemeSwitch } from "@/components/misc/theme-switch";
import { Logo } from "@/components/misc/icons";
import { Button } from "@heroui/button";
import { siteConfig } from "@/config/site";
import { link as linkStyles } from "@heroui/theme";
import NextLink from "next/link";
import clsx from "clsx";
import { useRouter } from "next/router";
import LinksMobile from "./linksMobile";
import { useState } from "react";

export const Navbar = () => {
  const router = useRouter();
  const [content, setContent] = useState<JSX.Element | null>(null);
  return (
    <NextUINavbar maxWidth="xl" position="sticky">
      <NavbarContent
        className="sm:flex basis-1/5 sm:basis-full"
        justify="start"
      >
        <Button color="secondary" variant="bordered" onPress={()=>{router.push("/auth")}}>Identificate</Button>
      </NavbarContent>
      <NavbarContent className="basis-1/5 sm:basis-full" justify="end">
        <NavbarBrand as="li" className="gap-3 max-w-[100%] lg:max-w-fit">
          <div className="flex justify-start items-center gap-1">
            <Logo />
            <p className="font-bold text-xl text-inherit tracking-[.25em]">DFC</p>
          </div>
        </NavbarBrand>
        <ul className="hidden lg:flex gap-4 justify-start ml-2">
          {siteConfig.navItems.map((item) => (
            <NavbarItem key={item.href}>
              <NextLink
                className={clsx(
                  linkStyles({ color: "foreground" }),
                  "data-[active=true]:text-primary data-[active=true]:font-medium",
                )}
                color="foreground"
                href={item.href}
              >
                {item.label}
              </NextLink>
            </NavbarItem>
          ))}
        </ul>
      </NavbarContent>
    </NextUINavbar>
  );
};
