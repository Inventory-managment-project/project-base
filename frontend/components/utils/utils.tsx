export const scrollTo = (section: string) => {
  const target = document.getElementById(section)
  if (target) {
    window.scrollTo({
      top: target.offsetTop - 20,
      behavior: "smooth"
    })
  }
}