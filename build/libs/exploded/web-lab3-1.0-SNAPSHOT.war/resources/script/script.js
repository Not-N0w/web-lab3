const styles = getComputedStyle(document.documentElement);

const color_red_bright = styles.getPropertyValue("--color-bright-red").trim();
const color_red = styles.getPropertyValue("--color-red").trim();
const color_light = styles.getPropertyValue("--color-light").trim();
const color_gray = styles.getPropertyValue("--color-gray").trim();
const color_darker = styles.getPropertyValue("--color-darker").trim();
const color_dark = styles.getPropertyValue("--color-darks").trim();

function updateGlobalValues(inX,inY,inR) {
    document.dispatchEvent(
        new CustomEvent(
            'updateGlobalValues',
            {
                detail: {
                    r: Number(inR),
                    x: inX,
                    y: inY
                }
            })
    );
}
function updateHits(inHits) {
    console.log(inHits)
    document.dispatchEvent(
        new CustomEvent(
            'updateHits',
            {
                detail: {
                    hits: inHits
                }
            })
    );
}