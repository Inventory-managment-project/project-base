import { Link } from "@heroui/link";

import { Head } from "./head";

import { Navbar } from "@/components/navbars/navbarLanding";

export default function LandingLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="relative flex flex-col h-screen">
      <Head />
      <Navbar />
      <main className="container mx-auto w-full h-full px-6 flex-grow pt-16">
        {children}
      </main>
    </div>
  );
}
