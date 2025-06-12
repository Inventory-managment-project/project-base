import { Link } from "@heroui/link";

import { Head } from "./head";

import { Navbar } from "@/components/navbars/navbarLanding";

export default function LandingLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="relative flex flex-col min-h-[400vh]">
      <Head />
      <Navbar />
      <main className="mx-auto w-full min-h-screen">
        {children}
      </main>
    </div>
  );
}
