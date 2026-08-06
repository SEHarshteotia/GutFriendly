const SIZE_STYLES = {
  sm: {
    mark: "h-9 w-9 rounded-xl text-sm",
    dot: "h-3 w-3",
    innerDot: "h-1.5 w-1.5",
    title: "text-base leading-none",
    subtitle: "text-[10px]",
    gap: "gap-2.5",
  },
  md: {
    mark: "h-11 w-11 rounded-2xl text-lg",
    dot: "h-4 w-4",
    innerDot: "h-2.5 w-2.5",
    title: "text-xl leading-none",
    subtitle: "text-[11px]",
    gap: "gap-3",
  },
  lg: {
    mark: "h-14 w-14 rounded-2xl text-xl",
    dot: "h-5 w-5",
    innerDot: "h-3 w-3",
    title: "text-3xl leading-none",
    subtitle: "text-xs",
    gap: "gap-3.5",
  },
};

const THEME_STYLES = {
  light: {
    mark: "bg-[#173F33] text-white shadow-sm",
    title: "font-bold tracking-tight text-[#173F33]",
    subtitle: "mt-1 font-medium uppercase tracking-widest text-gray-400",
  },
  dark: {
    mark: "bg-[#173F33] text-white shadow-sm",
    title: "font-bold tracking-tight text-white",
    subtitle: "mt-1 font-medium uppercase tracking-widest text-slate-400",
  },
  onDark: {
    mark:
      "border border-white/10 bg-white/14 text-white shadow-none backdrop-blur-sm",
    title: "font-bold tracking-tight text-white",
    subtitle: "mt-1 font-medium uppercase tracking-widest text-white/70",
  },
};

function LogoMark({ size, theme }) {
  const sizeStyle = SIZE_STYLES[size] ?? SIZE_STYLES.md;
  const themeStyle = THEME_STYLES[theme] ?? THEME_STYLES.light;

  return (
    <div
      className={`relative flex shrink-0 items-center justify-center font-extrabold tracking-tight ${sizeStyle.mark} ${themeStyle.mark}`}
      aria-hidden="true"
    >
      <span>G</span>
      <span
        className={`absolute -bottom-1 -right-1 flex items-center justify-center rounded-full border border-gray-100 bg-white ${sizeStyle.dot}`}
      >
        <span
          className={`rounded-full bg-emerald-500 ${sizeStyle.innerDot}`}
        />
      </span>
    </div>
  );
}

/**
 * Shared GutFriendly brand mark + wordmark.
 *
 * @param {'sm' | 'md' | 'lg'} size
 * @param {'light' | 'dark' | 'onDark'} theme
 * @param {string} [subtitle]
 * @param {boolean} [showWordmark=true]
 * @param {string} [href] - if set, renders as a link
 * @param {string} [className]
 * @param {string} [wordmarkClassName]
 */
export default function GutFriendlyLogo({
  size = "md",
  theme = "light",
  subtitle,
  showWordmark = true,
  href,
  className = "",
  wordmarkClassName = "",
}) {
  const sizeStyle = SIZE_STYLES[size] ?? SIZE_STYLES.md;
  const themeStyle = THEME_STYLES[theme] ?? THEME_STYLES.light;

  const content = (
    <>
      <LogoMark size={size} theme={theme} />
      {showWordmark && (
        <div className={wordmarkClassName}>
          <div className={themeStyle.title}>GutFriendly</div>
          {subtitle && (
            <div className={themeStyle.subtitle}>{subtitle}</div>
          )}
        </div>
      )}
    </>
  );

  const rootClassName = `inline-flex items-center ${sizeStyle.gap} ${className}`;

  if (href) {
    return (
      <a
        href={href}
        className={`${rootClassName} no-underline transition-opacity hover:opacity-85`}
        aria-label="Go to GutFriendly home"
      >
        {content}
      </a>
    );
  }

  return <div className={rootClassName}>{content}</div>;
}
