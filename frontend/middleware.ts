import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

const API_AUTH_URL = "http://172.18.0.4:8080/validate";

function decodeBase64(str: string) {
  return Buffer.from(str, "base64").toString("utf-8");
}

export async function middleware(req: NextRequest) {
  const rawToken = req.cookies.get("token")?.value;
  const token = rawToken ? decodeBase64(rawToken) : "";

  if (!token || token === "") {
    return NextResponse.redirect(new URL("/auth", req.url));
  }

  try {
    const response = await fetch(API_AUTH_URL, {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ token: token }),
    });

    if (!response.ok) {
      return NextResponse.redirect(new URL("/auth", req.url));
    }
  } catch (error) {
    console.error("Error durante la validación del token:", error);
    return NextResponse.redirect(new URL("/auth", req.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/panel/:path*"]
};
