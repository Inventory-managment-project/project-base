import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

function decodeBase64(str: string) {
  return Buffer.from(str, "base64").toString("utf-8");
}

export async function middleware(req: NextRequest) {
  const url = req.nextUrl.clone();
  const rawToken = req.cookies.get("token")?.value;
  const token = rawToken ? decodeBase64(rawToken) : "";

  const isAuthPath = url.pathname === "/auth";
  const isProtectedPath = url.pathname.startsWith("/panel");

  if (!token && isProtectedPath) {
    url.pathname = "/auth";
    return NextResponse.redirect(url);
  }

  if (token) {
    try {
      const response = await fetch(process.env.NEXT_PUBLIC_DOCKER_API_URL + "/validate", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ token: token }),
      });
      const isValid = response.ok;

      if (isAuthPath && isValid) {
        url.pathname = "/panel";
        return NextResponse.redirect(url);
      }
      if (!isValid) {
        const res = NextResponse.next();
        res.cookies.set("token", "", { maxAge: 0, path: "/" });

        if (isProtectedPath) {
          url.pathname = "/auth";
          return NextResponse.redirect(url);
        }

        return res;
      }
    } catch (err) {
      console.error("Error durante validación:", err);
      const res = NextResponse.next();
      res.cookies.set("token", "", { maxAge: 0, path: "/" });

      if (isProtectedPath) {
        url.pathname = "/auth";
        return NextResponse.redirect(url);
      }
      return res;
    }
  }
  return NextResponse.next();
}

export const config = {
  matcher: ["/panel/:path*", "/auth"],
};
